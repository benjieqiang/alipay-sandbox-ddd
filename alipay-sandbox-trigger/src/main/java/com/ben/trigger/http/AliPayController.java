package com.ben.trigger.http;

import com.alipay.api.internal.util.AlipaySignature;
import com.ben.domain.alipay.model.entity.PayOrderEntity;
import com.ben.domain.alipay.model.entity.ShopCartEntity;
import com.ben.domain.alipay.service.IOrderService;
import com.ben.types.enums.ResponseCode;
import com.ben.types.model.Response;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/alipay/")
public class AliPayController {

    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    @Resource
    private IOrderService orderService;
    @Resource
    private EventBus eventBus;

    /**
     * http://localhost:8091/api/v1/alipay/create_pay_order?userId=1001&productId=100001
     *
     * @param userId
     * @param productId
     * @return
     */
    @RequestMapping(value = "create_pay_order", method = RequestMethod.POST)
    public Response<String> createParOrder(@RequestParam String userId, @RequestParam String productId) {
        try {
            log.info("商品下单，根据商品ID创建支付单开始 userId:{} productId:{}", userId, productId);
            ShopCartEntity shopCartEntity = ShopCartEntity.builder().userId(userId).productId(productId).build();
            PayOrderEntity payOrderEntity = orderService.createOrder(shopCartEntity);
            log.info("商品下单，根据商品ID创建支付单完成 userId:{} productId:{} orderId:{}", userId, productId, payOrderEntity.getOrderId());
            return Response.<String>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(payOrderEntity.getPayUrl())
                    .build();
        } catch (Exception e) {
            log.error("商品下单，根据商品ID创建支付单失败 userId:{} productId:{}", userId, productId, e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
    /**
     * http://tcbcy4.natappfree.cc/api/v1/alipay/pay_notify
     * 支付宝往我们暴露出来的公网地址发消息，这样本地收到回调消息，更新订单状态到sucess，同时进行mq消息推送
     * @param request
     * @return
     */
    @RequestMapping(value = "pay_notify", method = RequestMethod.POST)
    public String payNotify(HttpServletRequest request) {
        try {
            log.info("支付回调，消息接收 {}", request.getParameter("trade_status"));
            if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
                Map<String, String> params = new HashMap<>();
                Map<String, String[]> requestParams = request.getParameterMap();
                for (String name : requestParams.keySet()) {
                    params.put(name, request.getParameter(name));
                }

                String tradeNo = params.get("out_trade_no");
                String gmtPayment = params.get("gmt_payment");
                String alipayTradeNo = params.get("trade_no");

                String sign = params.get("sign");
                String content = AlipaySignature.getSignCheckContentV1(params);
                boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayPublicKey, "UTF-8"); // 验证签名
                // 支付宝验签
                if (checkSignature) {
                    // 验签通过
                    log.info("支付回调，交易名称: {}", params.get("subject"));
                    log.info("支付回调，交易状态: {}", params.get("trade_status"));
                    log.info("支付回调，支付宝交易凭证号: {}", params.get("trade_no"));
                    log.info("支付回调，商户订单号: {}", params.get("out_trade_no"));
                    log.info("支付回调，交易金额: {}", params.get("total_amount"));
                    log.info("支付回调，买家在支付宝唯一id: {}", params.get("buyer_id"));
                    log.info("支付回调，买家付款时间: {}", params.get("gmt_payment"));
                    log.info("支付回调，买家付款金额: {}", params.get("buyer_pay_amount"));
                    log.info("支付回调，支付回调，更新订单 {}", tradeNo);
                    // 更新订单未已支付
                    orderService.updateOrderPaySuccess(tradeNo);
                    // 推送消息【自己的业务场景中可以使用MQ消息】
                    eventBus.post(tradeNo);
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("支付回调，处理失败", e);
            return "false";
        }
    }

}

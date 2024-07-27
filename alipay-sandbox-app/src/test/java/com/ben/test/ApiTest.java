package com.ben.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {
    // 「沙箱环境」应用ID - 您的APPID，收款账号既是你的APPID对应支付宝账号。获取地址；https://open.alipay.com/develop/sandbox/app
    public static String appId = "9021000139656222";
    // 「沙箱环境」商户私钥，你的PKCS8格式RSA2私钥
    public static String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCDKsd0psZt/q09GcPUv5b+7P6On/qPVfaOLGI5L71tk4PNUIha7egoUFaTB81U9y+pUq7A4IrpjROgRsNxsJZXrsBB/bWD++GzSNB5r/iDs0g6TrXzlUMU9afVR18tHAxvwutv4S3D1K7oFZf/Y9Rp5oumFwNN33DbZQXUkEJvoH8PHUsjVUtvuex3k1PKvpDO2trlvWsxG1Kup4DJArz6E4fCma1yL0MG9byQPCeFFoYC0yr+Yg+YfIH3ICmudc2h5wjLepQ53Rcn2t7oaXZK6/LA5piS3ZeZEt5wrScGLnukbiHweQTd5dyEixZh0XLGV8kieDpLzt/x9vqZW2CjAgMBAAECggEAYIpf+2+0pMjJ4SrRhAjvrfno8ji1RkJTEhmAwe5tUfU9ESSw95wbP3DCB726V0sH/WwpyzR5iaSYSWNL/qWmQisQvoFp1BbT7A0vxCDMnMKb6q58JSg7E3YrbUL2vlDipm+ksdfew0AK7C7YjNSBRuC08C4H9Iz8l74nKGh1PYqg8k8M6KybI3bJWXjZ1oPeiFmJGzWIWIC5m/Aju82aR8J1PDBbSU6WcAgMLQNKtQiQVVKJuX7H4bwmem0xluYECMUm08Dr16XR4wR45994V/u0rePMESJIjo8IPLDEh9ANXYu7nROqeh2w5APNelcYGU800f7Jp8MCuSTCUs0uoQKBgQDsXBjAp1xmK6oy0INXhJqv3byxR+OGTUCyr6vpnFr+xmKiIxtCILWLIPQwzNLF9pj260Jnmr3yivfy6xW5UVjQ6Ohewg7nTr2IvVWfK2ab6aBnM8Q4kf1CqkIs+SR2CD1Oge27+A9tXpLmeU1BizmANaVheAUXSti9FeneL9zt9QKBgQCOEP+T2QK/LkUyVnyjj8DMLcTCe30azQGGxcLuo1bZfsCvn60rDKQoCYJG+dMEknFjM6hKPEI4LkCwTJQbzux8FON+R3P0921fn37Wluc2OCyh2r6sZsi3WImgBKiibQJH1RLfOdKdIoKaqllQoliWXZ+rcYl4sQfpsG2vMQGdNwKBgQCXCmNm+Yw6XjztLAJCsyKGsBQc1eu3rzZg+ZHKP3iGxw/QvM52e6CHwA9MABfRGby2TbCptGEd1WCNg1zmh+1R0wRrSWdsuQy1jQhiJsHzcGBoktQZsnE9YL/ZLXz3UFydBrp9HA//vCfz0CBmeinMnHuKxb+7GsFWU+KXcc6k4QKBgD33eCNbIvGVEyWzx8XoLCZjSFG0X4tTJHlOxYolwPo5aX6xXW1LpEa6bxLaYYq6/a5Rl/RZeGDc++ZqYKgIh+pPijNIo3GUgyaUgVTEmbFyqzUmHslARAiFG/KqVYwTlE0UyZiIti9IIEOqTi73wUBFMiIr8dStE1CBXjZuX8/dAoGBANwj1svtw9yOLgNrfrXbP9AdCcDhmI36CxnDfpI+QKdY8hMVJSxgJtvz5vRulQ6H9JK3AL7QOK+BJkzQ0Y2C9ozP2ND48DOXPnPi55Ct2HbvOpJ4OxaxiCicolfSgVKc1xURkLkDwKbrGVGGxVPI4fqEQNBeuncmysK0sv+EDdRp";
    // 「沙箱环境」支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuwMLIJRlM5/abNEK8h69XPKprk0SP9x7ps5/cMG7sNbFu05pfQN3efOE7Fw9p3uHoXoLNio7dbTFjTxNVVD8S9eGTRc3zqYcULdV1hfOWF6PDx1LTeeGHFnHGBBeWGxALNNuh4XvwEFDA443yYT9OopmzwxmUspoRAT7x6kMPUwWwC9oZDyQKYEF/mSSHj5iouu8ZDoQz+zQZTI1YqqkFtMKTQbtsnakbGLS/MDZ7bow8JTQiL4rCZUGaMh+OrepURRQXOA8zKWgpAFe/r1gDvk57dy5+hsp0B5gx4x3+aSpYvUErNJEcEjtE1KSVfuIfjDSOOAWBQqiZYdGytXjlwIDAQAB";
    // 「沙箱环境」服务器异步通知回调地址
    public static String notify_url = "http://ppixcj.natappfree.cc/api/v1/alipay/alipay_notify_url";
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://www.baidu.com";
    // 「沙箱环境」
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // 签名方式
    public static String sign_type = "RSA2";
    // 字符编码格式
    public static String charset = "utf-8";

    private AlipayClient alipayClient;

    @Before
    public void init() {
        this.alipayClient = new DefaultAlipayClient(gatewayUrl,
                appId,
                PRIVATE_KEY,
                "json",
                charset,
                ALIPAY_PUBLIC_KEY,
                sign_type);
    }

    /**
     * @param :
     * @return void
     * @description 交易
     * @author benjieqiang
     * @date 2024/7/26 1:56 PM
     */
    @Test
    public void test_aliPay_pageExecute() throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();  // 发送请求的 Request类
        request.setNotifyUrl(notify_url);
        request.setReturnUrl(return_url);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "908417253529235er434");  // 我们自己生成的订单编号
        bizContent.put("total_amount", "100.44"); // 订单的总金额
        bizContent.put("subject", "测试商品");   // 支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");  // 固定配置
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        log.info("测试结果：{}", form);

        /**
         * 会生成一个form表单；
         * <form name="punchout_form" method="post" action="https://openapi-sandbox.dl.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.page.pay&sign=CAAYYDIbvUNRDvY%2B%2BF5vghx2dL9wovodww8CK0%2FferNP1KtyXdytBVLdZKssaFJV%2B8QksVuKlU3qneWhWUuI7atLDgzpussJlJhxTMYQ3GpAfOP4PEBYQFE%2FORemzA2XPjEn88HU7esdJdUxCs602kiFoZO8nMac9iqN6P8deoGWYO4UAwE0RCV65PKeJTcy8mzhOTgkz7V018N9yIL0%2BEBf5iQJaP9tGXM4ODWwFRxJ4l1Egx46FNfjLAMzysy7D14LvTwBi5uDXV4Y%2Bp4VCnkxh3Jhkp%2BDP9SXx6Ay7QaoerxHA09kwYyLQrZ%2FdMZgoQ%2BxSEOgklIZtYj%2FLbfx1A%3D%3D&return_url=https%3A%2F%2Fgaga.plus&notify_url=http%3A%2F%2Fngrok.sscai.club%2Falipay%2FaliPayNotify_url&version=1.0&app_id=9021000132689924&sign_type=RSA2&timestamp=2023-12-13+11%3A36%3A29&alipay_sdk=alipay-sdk-java-4.38.157.ALL&format=json">
         * <input type="hidden" name="biz_content" value="{&quot;out_trade_no&quot;:&quot;100001001&quot;,&quot;total_amount&quot;:&quot;1.00&quot;,&quot;subject&quot;:&quot;测试&quot;,&quot;product_code&quot;:&quot;FAST_INSTANT_TRADE_PAY&quot;}">
         * <input type="submit" value="立即支付" style="display:none" >
         * </form>
         * <script>document.forms[0].submit();</script>
         */
    }

    /**
     * 查询订单，使用内置的response接收；
     */
    @Test
    public void test_alipay_execute() throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo("908417253529235er434");
        request.setBizModel(bizModel);

        AlipayTradeQueryResponse response = alipayClient.execute(request);
        String tradeNo = response.getTradeNo();
        System.out.println(tradeNo);
        log.info("测试结果：{}", JSON.toJSONString(response));
    }

    /**
     * @param :
     * @return void
     * @description 关单接口
     * https://opendocs.alipay.com/open/8dc9ebb3_alipay.trade.close?scene=common&pathHash=0c042d2b
     * @author benjieqiang
     * @date 2024/7/27 2:29 PM
     */
    @Test
    public void test_closeOrder() throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("trade_no", "908417253529235er434");
        request.setBizContent(bizContent.toString());
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        log.info("订单关闭结果： {}", JSON.toJSONString(response));
    }
    /**
     * 退款接口
     * https://opendocs.alipay.com/open/f60979b3_alipay.trade.refund?scene=common&pathHash=e4c921a7
     *
     */
    @Test
    public void test_alipay_refund() throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("9084172535292354");
        refundModel.setRefundAmount("23.23"); // 和数据库中一致
        refundModel.setRefundReason("退款说明");
        request.setBizModel(refundModel);

        AlipayTradeRefundResponse rsp = alipayClient.execute(request);
        log.info("测试结果：{}", JSON.toJSONString(rsp));
    }


}

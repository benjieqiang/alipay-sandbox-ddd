package com.ben.test;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
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
    public static String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMw8d2p6uLPB0RZYI4r7KzuKBoZHyKXnK+78Cgh2WUO+IvMdNsXvlntYLnPqsiJ3u/RjKUJ2w+8HigOHZQSi/FIP3YdYJ7kv0AVqR4qtg9InNUL98x4ymJon/1G4gb8tDEKLtZiuJLSJMbvj5i2Q08BcNNjVzV8AB5Dfo3+yJvzJQIlBftqyuqr4Frv6mJ79A+haanU3S+PIGzLY5TvfNgw80apoTKJUMzbdDjaiN2n4qdfupf/CUsYHYHWU2sLEtDKmH0S7ytbOHbvty5i0szekSWD+EO4NRTdMfK55qFmcHQ5JIiUOfAYc+Ya0fmrEu+t+dqucFaDpqC6nbEfk6ZAgMBAAECggEAPfE9Ccq4oxl91RTtG9K8+XcKJ0Xow97R8ZGBG2LZYUSEwK3VUn/skiV+iBG8rtffDYlvYmCCsqORATg3YBd1M/LToXf8RjKlYIu/7lWHuEc7ptU2CzbiJW27jvlhiKLuGROvdbS25df23CN4Qnku6LXEfpDxRDdLjfmHnTDGs5kQHcYpzAlBBwVTS6mJOBrSl93CtjrslF28UF5VyqKUrw+O4I4fhziEcVoV4i5Z9vHgoYcMhX6nLlCQ/xJN3EVp9Ce9eEf6a1JPIPYNln3tOL+jK3bWtZZPHtJF9cAn9uAyHCwTQdX6EHxBnZEWoL/OZsaplCOlEyXR2gDoO28TAQKBgQDu6FJRPmSUbeWwcymw3qgzckvBIo8d/dWk6dmtAJwLR7MR3GMqIzzwb1EG8V0LBrqVitaLXPRr4c7+TkRRDP6eirv6NCbBzUPVEL9zja+9/vZJUxF/5OMuNHK2AWhWKQ9Is1Fxa9LpKC9Zp9TsHYb+4fEp2inYI2WVSD/eo4d4YQKBgQCW1e/z9HYrk4w3ho4ZQBdXv1MuftTgz5ihBsR/5HqehU7IAdRSTf8T5HI6A3u1dRa3RwQs3tZu9Oigeto7PjR/vsNgw6fc+cXEX4GO/+vjN0uWZJewvcperc6IRnt/NLoLW9FS/gLR6cOI0oz3D7Q+PK5zQhj3yop5ohM91tAhOQKBgAh+Zf7Rcw374kClt4O8Rsqdc6gILIJqdeAJGF4YOXQFNo6aAUSMQCxkKMNQbXho4tVSPFJ0GZSEULGl4rPm/F5ynEBG/mHBhHncwJHBJdBhciwlMegUISeRYcqqviTiDNMO3QRlSvhTH2vWlbFXU2dYbRSJ1xcCGyh1ix/FPJnBAoGBAIMxLPW5CRc5zFl3NR0NO8BcbtMgCbRHm+9i3YKHjYdXV4Bq5ut3X4dDyX5gDTd00f8zS5RjdYsohTUf5bRllHAP0gvV4ak3riXxE9T3D4qfS6VRdQaK7PuQnPS2qS+pArBiehx/RqChfCsDRwyUoicHzIv/T4wmpG7K44WD058xAoGATmBYA0kWiNDtDuHavrxoDWeXqfv8r7AS39Go1F8/P8/LBjoeefa8fsTI33X0wniOT6k3jwQaH5M9TZB4PBy3ucsetDHhJoCQL8uoUsJxS2IOR66L3SKB66hbfvx1++jVH6s7gmJeOzHFg4bn0iq557ILPhODtCJHISaypi3GOeM=";
    // 「沙箱环境」支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuwMLIJRlM5/abNEK8h69XPKprk0SP9x7ps5/cMG7sNbFu05pfQN3efOE7Fw9p3uHoXoLNio7dbTFjTxNVVD8S9eGTRc3zqYcULdV1hfOWF6PDx1LTeeGHFnHGBBeWGxALNNuh4XvwEFDA443yYT9OopmzwxmUspoRAT7x6kMPUwWwC9oZDyQKYEF/mSSHj5iouu8ZDoQz+zQZTI1YqqkFtMKTQbtsnakbGLS/MDZ7bow8JTQiL4rCZUGaMh+OrepURRQXOA8zKWgpAFe/r1gDvk57dy5+hsp0B5gx4x3+aSpYvUErNJEcEjtE1KSVfuIfjDSOOAWBQqiZYdGytXjlwIDAQAB";
    // 「沙箱环境」服务器异步通知回调地址
    public static String notify_url = "https://xfg.natapp.cn/api/v1/alipay/alipay_notify_url";
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
        bizContent.put("out_trade_no", "2423AAA000032333361X04");  // 我们自己生成的订单编号
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
     * 查询订单
     */
    @Test
    public void test_alipay_certificateExecute() throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
        bizModel.setOutTradeNo("2423AAA000032333361X04");
        request.setBizModel(bizModel);

        String body = alipayClient.certificateExecute(request).getBody();
        log.info("测试结果：{}", body);
    }

    /**
     * 退款接口
     */
    @Test
    public void test_alipay_refund() throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("2423AAA000032333361X04");
        refundModel.setRefundAmount("1.00");
        refundModel.setRefundReason("退款说明");
        request.setBizModel(refundModel);

        AlipayTradeRefundResponse execute = alipayClient.execute(request);
        log.info("测试结果：{}", execute.isSuccess());
    }


}

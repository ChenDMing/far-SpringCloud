package own.mongodb.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import own.mongodb.entity.vo.OrderItem;
import own.mongodb.service.TestRestTemplateService;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class TestRestTemplateServiceImpl implements TestRestTemplateService {

    /**
     * 获取物流对接的token
     */
    @Override
    public ResponseEntity<JSONObject> testRest() {
        // 对接物流需要先请求获取令牌, 再将 token 作为参数用于其他的请求
        // token 有效期是10天, 系统中可配置, 定时任务9天跑一次, 获取一次新的令牌
        String url = "https://apiv2.shiprocket.in/v1/external/auth/login";
        HttpMethod method = HttpMethod.POST;
        Map<String, Object> params = new HashMap<>();
        params.put("email", "test@111.com");
        params.put("password", "test123456");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 设置提交方式, 一般使用json或者表单, 对接印度物流需要的是 json 方式
        headers.setContentType(MediaType.APPLICATION_JSON);
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 请求头和参数
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);

        // 发送请求
        ResponseEntity<JSONObject> responseEntity = restTemplate.exchange(url, method, requestEntity, JSONObject.class);
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        if (responseEntity.getStatusCodeValue() == 200) {
            JSONObject body = responseEntity.getBody();
            String token = StringUtils.isEmpty(body) ? "" : body.getString("token");
            System.out.println("---------------------token------------------------");
            System.out.println(token);
            System.out.println("---------------------token------------------------");
        }
        return responseEntity;
    }

    /**
     * 创建订单
     */
    @Override
    public ResponseEntity<JSONObject> createOrder(String token) {
        String url = "https://apiv2.shiprocket.in/v1/external/orders/create/adhoc";
        ResponseEntity<JSONObject> responseEntity = this.sendRequest(url, token, HttpMethod.POST, getCreateOrderParam());
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        int status = responseEntity.getStatusCodeValue();
        JSONObject body = responseEntity.getBody();
        return responseEntity;
    }

    /**
     * 查询单个订单
     */
    @Override
    public ResponseEntity<JSONObject> getSingleOrder(String token, String orderId) {
        String url = "https://apiv2.shiprocket.in/v1/external/orders/show/" + orderId;
        // 发送请求
        ResponseEntity<JSONObject> responseEntity = this.sendRequest(url, token, HttpMethod.GET, null);
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        return responseEntity;
    }

    /**
     * 获取所有订单
     * @return orderList
     */
    @Override
    public ResponseEntity<JSONObject> getAllOrder(String token) {
        String url = "https://apiv2.shiprocket.in/v1/external/orders";
        // 发送请求
        ResponseEntity<JSONObject> responseEntity = this.sendRequest(url, token, HttpMethod.GET, null);
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        return responseEntity;
    }

    /**
     * 取消订单
     * @return cancelResult
     */
    @Override
    public ResponseEntity<JSONObject> cancelOrder(String token, String orderString) {
        String url = "https://apiv2.shiprocket.in/v1/external/orders/cancel";
        Map<String, Object> params = new HashMap<>();
        String[] arr = new String[]{"28799617"};
        params.put("ids", arr);
        ResponseEntity<JSONObject> responseEntity = this.sendRequest(url, token, HttpMethod.POST, params);
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        return responseEntity;
    }

    /**
     * 导出订单
     */
    @Override
    public ResponseEntity<JSONObject> exportAllOrder(String token) {
        String url = "https://apiv2.shiprocket.in/v1/external/orders/export";
        ResponseEntity<JSONObject> responseEntity = this.sendRequest(url, token, HttpMethod.POST, null);
        log.info("物流响应 {}", JSONObject.toJSONString(responseEntity));
        return responseEntity;
    }

    /**
     * 拼接请求基本信息
     * @return response
     */
    private ResponseEntity<JSONObject> sendRequest(String url, String token, HttpMethod method, Map<String, Object> params) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Map<String, Object>> requestEntity;
        if (CollectionUtils.isEmpty(params)) {
            requestEntity = new HttpEntity<>(headers);
        } else {
            requestEntity = new HttpEntity<>(params, headers);
        }
        // 发送请求
        return restTemplate.exchange(url, method, requestEntity, JSONObject.class);
    }

    /**
     * 创建订单参数拼接
     */
    private Map<String, Object> getCreateOrderParam() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 拼接参数
        Map<String, Object> params = new HashMap<>();
        params.put("order_id", System.currentTimeMillis());
        params.put("order_date", dateFormat.format(new Date()));
        params.put("pickup_location", "test-add");
        params.put("billing_customer_name", "ChenDM"); //Naruto
        params.put("billing_last_name", "df"); //""
        params.put("billing_address", "s h f y u l"); //House 221B, Leaf Village
        params.put("billing_city", "ShaHai"); //New Delhi
        params.put("billing_pincode", "110002");
        params.put("billing_state", "PuDong"); //Delhi
        params.put("billing_country", "Chine"); //India
        params.put("billing_email", "naruto@uzumaki.com");
        params.put("billing_phone", "9876543210");
        params.put("shipping_is_billing", true);
        OrderItem orderItem = new OrderItem("Kunai", "chakra123", "2", "90");
        params.put("order_items", Collections.singletonList(orderItem));
        params.put("payment_method", "Prepaid"); //COD
        params.put("sub_total", 180); //产品价格乘数量
        // 包裹规格-必填
        params.put("length", 10);
        params.put("breadth", 15);
        params.put("height", 20);
        params.put("weight", 2.5);
        return params;
    }

    /**
     * 配送成功回调
     */
    @Override
    public void callBackShipRocket(String data) {
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONObject currentTimestamp = jsonObject.getJSONObject("current_timestamp");
        JSONArray scans = jsonObject.getJSONArray("scans");
        for (Object scan : scans) {
            JSONObject scanItem = JSONObject.parseObject(JSONObject.toJSONString(scan));
        }
        log.info("订单跟踪回调数据: {}", data);
    }
}

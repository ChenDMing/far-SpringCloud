package own.mongodb.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

public interface TestRestTemplateService {
    ResponseEntity<JSONObject> testRest();

    ResponseEntity<JSONObject> createOrder(String token);

    ResponseEntity<JSONObject> getAllOrder(String token);

    ResponseEntity<JSONObject> getSingleOrder(String token, String orderId);

    ResponseEntity<JSONObject> cancelOrder(String token, String orderString);

    ResponseEntity<JSONObject> exportAllOrder(String token);

    void callBackShipRocket(String json);
}

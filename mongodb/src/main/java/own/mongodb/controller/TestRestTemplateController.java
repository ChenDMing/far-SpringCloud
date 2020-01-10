package own.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import own.common.enumerate.Msg;
import own.common.utils.ResponseEntity;
import own.common.utils.ResultUtils;
import own.mongodb.entity.dto.OrderTraceDTO;
import own.mongodb.service.TestRestTemplateService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/test/rest")
public class TestRestTemplateController {

    private final TestRestTemplateService testRestTemplateService;

    @Autowired
    TestRestTemplateController(TestRestTemplateService testRestTemplateService) {
        this.testRestTemplateService = testRestTemplateService;
    }

    /**
     * 获取物流对接的授权token
     * @return result
     */
    @RequestMapping(value = "/get/token", method = RequestMethod.POST)
    public ResponseEntity testRest() {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.testRest());
    }

    /**
     * 创建订单
     * @return result
     */
    @RequestMapping(value = "/create/order", method = RequestMethod.POST)
    public ResponseEntity createOrder(String token) {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.createOrder(token));
    }

    /**
     * 获取所有订单
     * @return result
     */
    @RequestMapping(value = "/all/order", method = RequestMethod.GET)
    public ResponseEntity getAllOrderDetail(String token) {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.getAllOrder(token));
    }

    /**
     * 获取单个订单
     * @return result
     */
    @RequestMapping(value = "/single/order", method = RequestMethod.GET)
    public ResponseEntity getSingleOrder(String token, String orderId) {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.getSingleOrder(token, orderId));
    }

    /**
     * 取消订单
     */
    @RequestMapping(value = "/cancel/order", method = RequestMethod.POST)
    public ResponseEntity cancelOrder(String token, String orderString) {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.cancelOrder(token, orderString));
    }

    /**
     * 导出订单
     * @return result
     */
    @RequestMapping(value = "/export/order", method = RequestMethod.POST)
    public ResponseEntity exportAllOrder(String token) {
        return ResultUtils.resultMsg(Msg.Success, testRestTemplateService.exportAllOrder(token));
    }

    /**
     * 跟踪回调
     */
    @RequestMapping(value = "/call/webHook", method = RequestMethod.POST)
    public ResponseEntity callBackShipRocket(@RequestBody String data) {
        testRestTemplateService.callBackShipRocket(data);
        return ResultUtils.resultMsg(Msg.Success);
    }

}

package com.human.graduateproject.controller.momo;

import com.human.graduateproject.dto.OrderDto;
import com.human.graduateproject.services.momo.MoMoPaymentService;
import com.human.graduateproject.util.momo.MoMoPaymentRequest;
import com.human.graduateproject.util.momo.MoMoUtil;
import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/api/momo")
public class MoMoController {

    private final MoMoPaymentService moMoPaymentService;

    private final String partnerCode = "MOMO";
    private final String accessKey = "F8BBA842ECF85";
    private final String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    //private final String redirectUrl = "https://momo.vn/return";
    @Value("${frontend.redirect-url}")
    private String redirectUrl;// cau hinh o properties
    private final String ipnUrl = "https://callback.url/notify";
    private final String requestType = "captureWallet";
    private final String momoEndpoint = "https://test-payment.momo.vn/v2/gateway/api/create";

    public MoMoController(MoMoPaymentService moMoPaymentService) {
        this.moMoPaymentService = moMoPaymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestParam String amount, @RequestParam Long systemOrderId) {
        try {
            String requestId = partnerCode + UUID.randomUUID();

            String orderId = systemOrderId +requestId;

            String orderInfo = "Thanh toán MoMo";

            // Chuẩn bị dữ liệu để ký
            String rawData = "accessKey=" + accessKey +
                    "&amount=" + amount +
                    "&extraData=" +
                    "&ipnUrl=" + ipnUrl +
                    "&orderId=" + orderId +
                    "&orderInfo=" + orderInfo +
                    "&partnerCode=" + partnerCode +
                    "&redirectUrl=" + redirectUrl +
                    "&requestId=" + requestId +
                    "&requestType=" + requestType;

            // Ký HMAC SHA256
            String signature = MoMoUtil.signHmacSHA256(rawData, secretKey);

            // Tạo request object
            MoMoPaymentRequest momoRequest = new MoMoPaymentRequest(partnerCode, accessKey, requestId, amount, orderId, orderInfo,
                    redirectUrl, ipnUrl, "", requestType, signature);

            // Gửi request đến MoMo
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(new Gson().toJson(momoRequest), headers);

            ResponseEntity<String> response = restTemplate.exchange(momoEndpoint, HttpMethod.POST, entity, String.class);

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/handle/after-payment")
    public ResponseEntity<?> handleAfterPayment(@RequestParam Long resultCode,@RequestParam Long systemOrderId,@RequestParam String momoTransactionId) {

        try {

            boolean isUpdate;
            if (resultCode==0) {
                isUpdate = moMoPaymentService.updateOrderAfterMomoSuccess(systemOrderId, momoTransactionId);

            }
            else {
                isUpdate = moMoPaymentService.updateOrderAfterMomoFailed(systemOrderId, momoTransactionId);
            }
            return ResponseEntity.ok(isUpdate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }

}

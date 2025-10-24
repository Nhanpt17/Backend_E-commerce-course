package com.human.graduateproject.util.momo;

import lombok.Data;

@Data
public class MoMoPaymentRequest {

    private String partnerCode;
    private String accessKey;
    private String requestId;
    private String amount;
    private String orderId;
    private String orderInfo;
    private String redirectUrl;
    private String ipnUrl;
    private String extraData;
    private String requestType;
    private String signature;
    private String lang = "en"; // mặc định là tiếng Anh

    // Constructor
    public MoMoPaymentRequest(String partnerCode, String accessKey, String requestId, String amount, String orderId,
                              String orderInfo, String redirectUrl, String ipnUrl, String extraData,
                              String requestType, String signature) {
        this.partnerCode = partnerCode;
        this.accessKey = accessKey;
        this.requestId = requestId;
        this.amount = amount;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.redirectUrl = redirectUrl;
        this.ipnUrl = ipnUrl;
        this.extraData = extraData;
        this.requestType = requestType;
        this.signature = signature;
    }

}

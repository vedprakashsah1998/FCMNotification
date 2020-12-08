package com.infinity8.fcmnotificationpractice.network;


import com.infinity8.fcmnotificationpractice.model.NotificationReq;
import com.infinity8.fcmnotificationpractice.model.NotificationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface NotificationRequest {


    @Headers({"Content-Type:application/json","Authorization:key=AAAAu5L0RWs:APA91bF_veCzzAr02T5_9upjU_ZKTxtxxCLe_joqtlmRnotWDU9s8C_t2DLGlgx305oFt5iynX2kLzYYqUXtQHmEpeDzNZT_udeO1peDdsiIvLrRHt9K_Sj-mnSYJZRig0iWFsOXJyMQ"})
    @POST("send")
    Call<NotificationResponse> send(@Body NotificationReq request);

}

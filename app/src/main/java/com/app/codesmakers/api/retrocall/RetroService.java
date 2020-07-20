package com.app.codesmakers.api.retrocall;

import com.app.codesmakers.api.contstants.Params;
import com.app.codesmakers.api.pojo.ads.AdvertisementResponse;
import com.app.codesmakers.api.pojo.carriers.MyCarrierResponse;
import com.app.codesmakers.api.pojo.chat.ChatResponse;
import com.app.codesmakers.api.pojo.config.ConfigurationResponse;
import com.app.codesmakers.api.pojo.courieroffers.CourierOffersModel;
import com.app.codesmakers.api.pojo.favplaces.FavPlaceResponse;
import com.app.codesmakers.api.pojo.feedback.FeedbackResponse;
import com.app.codesmakers.api.pojo.menu.ProductResponse;
import com.app.codesmakers.api.pojo.myorders.MyOrderResponse;
import com.app.codesmakers.api.pojo.notification.NotificationResponse;
import com.app.codesmakers.api.pojo.order.OrderResponse;
import com.app.codesmakers.api.pojo.profile.ProfileResponse;
import com.app.codesmakers.api.pojo.request.LoginResponse;
import com.app.codesmakers.api.pojo.store.StoreResponse;
import com.app.codesmakers.api.pojo.track.TrackOderResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroService {

    @FormUrlEncoded
    @POST("InstallingSetting.php")
    Observable<List<ConfigurationResponse>> getConfigurations(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName
    );

    @FormUrlEncoded
    @POST("Login.php")
    Observable<List<LoginResponse>> loginWithPhone(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_PHONE_NUMBER) String phoneNumbers);

    @FormUrlEncoded
    @POST("Profile.php")
    Observable<List<ProfileResponse>> getUserAccount(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("Feddback.php")
    Observable<List<FeedbackResponse>> getFeedback(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("FavList.php")
    Observable<List<FavPlaceResponse>> getFavPlaces(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("NewFav.php")
    Observable<List<FavPlaceResponse>> addFavPlace(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_FAV_NAME) String favName,
            @Field(Params.FIELD_FAV_LOCATION) String favLoc,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("Updatefav.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> unFavPlace(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_STATUS) String status,
            @Field(Params.FIELD_FAV_ID) String favId,
            @Field(Params.FIELD_APP_NAME) String appName);


    @FormUrlEncoded
    @POST("Home.php?RequestNum")
    Observable<List<AdvertisementResponse>> getHomeList(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("Stores.php?D=Nearby")
    Observable<List<StoreResponse>> getStoreList(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("OrdersList.php?D=Myorders&CarID")
    Observable<List<MyOrderResponse>> getMyOrderList(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("OrdersList.php?D=Nearby&CarID")
    Observable<List<MyCarrierResponse>> nearbyAvailableOrders(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("NewOffer.php?D=Nearby&CarID")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> postNewOffer(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderId,
            @Field(Params.FIELD_TRACK_OFFER) String offer);

    @FormUrlEncoded
    @POST("NotificationList.php")
    Observable<List<NotificationResponse>> getNotificationList(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("Products.php?RequestNum&search")
    Observable<List<ProductResponse>> getStoreMenu(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_LOCATION) String userLocation,
            @Field(Params.FIELD_STORE_ID) String storeId);

    @FormUrlEncoded
    @POST("NewOrder.php")
    Observable<List<OrderResponse>> newOrder(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_CONTENT) String content,
            @Field(Params.FIELD_PRODUCTS) String productIds,
            @Field(Params.FIELD_STORE_ID) String storeId,
            @Field(Params.FIELD_PRICE) String price,
            @Field(Params.FIELD_DURATION) String duration,
            @Field(Params.FIELD_COUPON_CODE) String couponCode);

    @FormUrlEncoded
    @POST("OrderInfo.php")
    Observable<List<TrackOderResponse>> getOrderInfo(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID);

    @FormUrlEncoded
    @POST("offerlist.php")
    Observable<List<CourierOffersModel>> getOfferList(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID);

    @FormUrlEncoded
    @POST("UpdateOffer.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> updateOffer(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_OFFER_ID) String offerID,
            @Field(Params.FIELD_STATUS) String statues,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("UpdateOrder.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> updateOrder(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_STATUS) String statues,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("NewFeedback.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> sendFeedback(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_COMMENT) String comment,
            @Field(Params.FIELD_RATE_STORE) String rateStore,
            @Field(Params.FIELD_RATE_USER) String rateUser,
            @Field(Params.FIELD_TO_USER) String toUser,
            @Field(Params.FIELD_TO_STORE) String toStore,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderId,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("NewComplaint.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> newComplaint(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_CONTENT) String content,
            @Field(Params.FIELD_IMG) String img,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID,
            @Field(Params.FIELD_APP_NAME) String appName);


    @FormUrlEncoded
    @POST("NewFeedback.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> newFeedback(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_COMMENT) String comment,
            @Field(Params.FIELD_RATE_STORE) String rateStore,
            @Field(Params.FIELD_RATE_USER) String rateUser,
            @Field(Params.FIELD_TO_USER) String toUser,
            @Field(Params.FIELD_TO_STORE) String toStore,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID,
            @Field(Params.FIELD_APP_NAME) String appName);

    @FormUrlEncoded
    @POST("Chats.php")
    Observable<List<ChatResponse>> getChat(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID);

    @FormUrlEncoded
    @POST("NewChat.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> sendMessage(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_CONTENT) String content,
            @Field(Params.FIELD_TRACK_ORDER_ID) String orderID,
            @Field(Params.FIELD_IMG) String img,
            @Field(Params.FIELD_BILL) String bill
    );

    @FormUrlEncoded
    @POST("UpdateProfile.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> updateProfile(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_NEW_NAME) String newName,
            @Field(Params.FIELD_NEW_PHONE) String newPhone,
            @Field(Params.FIELD_NEW_EMAIL) String newEmail,
            @Field(Params.FIELD_NEW_PHOTO) String newPhoto,
            @Field(Params.FIELD_NEW_LANG) String newLang);


    @FormUrlEncoded
    @POST("Basic/receivelog.php")
    Observable<List<com.app.codesmakers.api.pojo.ResponseBody>> updateLog(
            @Field(Params.FIELD_API_KEY) String apiKey,
            @Field(Params.FIELD_APP_MODEL_TYPE) String appModelType,
            @Field(Params.FIELD_APP_MODEL_VERSION) String modelVersion,
            @Field(Params.FIELD_DEVICE_ID) String deviceId,
            @Field(Params.FIELD_DEVICE_TYPE) String deviceType,
            @Field(Params.FIELD_OS_PLAYER_ID) String osPlayerId,
            @Field(Params.FIELD_APP_NAME) String appName,
            @Field(Params.FIELD_USER_ID) String userId,
            @Field(Params.FIELD_USER_LOCATION) String location,
            @Field(Params.FIELD_NEW_LOG) String newPhoto);
}
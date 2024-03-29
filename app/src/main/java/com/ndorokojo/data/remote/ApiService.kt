package com.ndorokojo.data.remote

import com.ndorokojo.data.models.AllEventResponse
import com.ndorokojo.data.models.AllLimbahResponse
import com.ndorokojo.data.models.AllPakanResponse
import com.ndorokojo.data.models.AllRasTernakResponse
import com.ndorokojo.data.models.AllTernakListResponse
import com.ndorokojo.data.models.BirthResponse
import com.ndorokojo.data.models.BuyPayload
import com.ndorokojo.data.models.NewsResponse
import com.ndorokojo.data.models.BuyResponse
import com.ndorokojo.data.models.DetailKandangResponse
import com.ndorokojo.data.models.DetailNewsResponse
import com.ndorokojo.data.models.DiedResponse
import com.ndorokojo.data.models.DistrictResponse
import com.ndorokojo.data.models.KandangPayload
import com.ndorokojo.data.models.ListEventResponse
import com.ndorokojo.data.models.ListKandangResponse
import com.ndorokojo.data.models.LiveStockItem
import com.ndorokojo.data.models.LoginResponse
import com.ndorokojo.data.models.LogoutResponse
import com.ndorokojo.data.models.NegotiationResponse
import com.ndorokojo.data.models.NotificationResponse
import com.ndorokojo.data.models.ProfileInfoResponse
import com.ndorokojo.data.models.ProvinceResponse
import com.ndorokojo.data.models.RegencyResponse
import com.ndorokojo.data.models.RegisterResponse
import com.ndorokojo.data.models.SearchResponse
import com.ndorokojo.data.models.SellResponse
import com.ndorokojo.data.models.SliderCategoryResponse
import com.ndorokojo.data.models.StoreKandangResponse
import com.ndorokojo.data.models.StoreTernakFreeResponse
import com.ndorokojo.data.models.StoreTernakPayload
import com.ndorokojo.data.models.StoreTernakResponse
import com.ndorokojo.data.models.UpdateImageResponse
import com.ndorokojo.data.models.UpdatePenawaranResponse
import com.ndorokojo.data.models.UpdateProfileResponse
import com.ndorokojo.data.models.UpdateTernakResponse
import com.ndorokojo.data.models.VillageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("username") email: String,
        @Field("password") password: String
    ): LoginResponse

    @POST("register")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @PUT("profile")
    @FormUrlEncoded
    suspend fun updateUser(
        @Field("fullname") fullname: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("address") address: String,
        @Field("occupation") occupation: String,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("kelompok_ternak") kelompok_ternak: String,
        @Field("province_id") province_id: String,
        @Field("regency_id") regency_id: String,
        @Field("district_id") district_id: String,
        @Field("village_id") village_id: String,
    ): UpdateProfileResponse

    @POST("logout")
    suspend fun logoutUser(
    ): LogoutResponse

    @GET("profile")
    suspend fun getProfileInfo(): ProfileInfoResponse

    @GET("references/provinces")
    suspend fun getAllProvinces(): ProvinceResponse

    @GET("references/regencies")
    suspend fun getAllRegencies(
        @Query("province_id") province_id: Int
    ): RegencyResponse

    @GET("references/districts")
    suspend fun getAllDistricts(
        @Query("regency_id") regency_id: Int
    ): DistrictResponse

    @GET("references/villages")
    suspend fun getAllVillages(
        @Query("district_id") district_id: Int
    ): VillageResponse

    @GET("livestocks")
    suspend fun getListTernak(
        @Query("livestock_type_id") livestock_type_id: Int
    ): AllTernakListResponse

    @GET("references/livestock-types")
    suspend fun getAllRasTernakList(): AllRasTernakResponse

    @GET("kandang")
    suspend fun getListKandang(
        @Query("type_id") type_id: Int
    ): ListKandangResponse

    @POST("livestocks/store")
    suspend fun storeTernak(@Body payload: StoreTernakPayload): StoreTernakResponse

    @POST("kandang/store")
    suspend fun storeKandang(
        @Body payload: KandangPayload
    ): StoreKandangResponse

    @GET("references/limbah")
    suspend fun getAllLimbah(): AllLimbahResponse

    @GET("references/pakan")
    suspend fun getAllPakan(): AllPakanResponse

    @POST("livestocks/births")
    @FormUrlEncoded
    suspend fun birthTernak(
        @Field("kandang_id") kandang_id: Int,
        @Field("pakan") pakan_id: String,
        @Field("limbah_id") limbah_id: Int,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("type_id") type_id: Int,
        @Field("nominal") nominal: Int,
    ): BirthResponse

    @POST("livestocks/deads")
    @FormUrlEncoded
    suspend fun diedTernak(
        @Field("id") id: Int,
        @Field("dead_type") dead_type: String,
        @Field("dead_reason") dead_reason: String,
        @Field("dead_year") dead_year: String,
        @Field("dead_month") dead_month: String,
    ): DiedResponse

    @POST("transactions/sells")
    @FormUrlEncoded
    suspend fun sellTernak(
        @Field("id") id: Int,
        @Field("proposed_price") proposed_price: Int
    ): SellResponse

    @GET("transactions/events")
    suspend fun getAllListEvents(): ListEventResponse

    @POST("transactions/buys")
//    @FormUrlEncoded
    suspend fun buyTernak(
//        @Field("livestocks") livestocks: Array<LiveStockItem>,
//        @Field("kandang_id") kandang_id: Int,
//        @Field("deal_price") deal_price: Int,
        @Body payload: BuyPayload
    ): BuyResponse

    @GET("sliders/today")
    suspend fun getAllBrebesToday(): NewsResponse

    @GET("sliders/finance")
    suspend fun getAllFinanceToday(): NewsResponse

    @GET("sliders/{slug}/{id}")
    suspend fun getDetailNews(
        @Path("slug") slug: String,
        @Path("id") id: Int,
    ): DetailNewsResponse

//    @GET("sliders/finance/{id}")
//    suspend fun getFinanceToday(
//        @Path("id") id: Int
//    ): DetailNewsResponse

    @GET("report/by-kandang-id")
    suspend fun getDetailKandang(
        @Query("kandang_id") kandang_id: Int
    ): DetailKandangResponse

    @POST("livestocks/status/update")
    @FormUrlEncoded
    suspend fun updateTernak(
        @Field("id") id: Int,
        @Field("status") status: String,
        @Field("year") year: String,
        @Field("month") month: String,
    ): UpdateTernakResponse

    @GET("search")
    suspend fun searchQuery(
        @Query("search") search: String
    ): SearchResponse

    @Multipart
    @POST("livestocks/image/update")
    suspend fun updateImage(
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part,
    ): UpdateImageResponse

    @GET("transactions/proposals")
    suspend fun getNotifications(): NotificationResponse

    @POST("transactions/proposals/{id}")
    @FormUrlEncoded
    suspend fun updatePenawaran(
        @Path("id") id: Int,
        @Field("status") status: String
    ): UpdatePenawaranResponse

    @GET("references/slider-categories")
    suspend fun getSliderCategories(): SliderCategoryResponse

    @GET("notifications/negotiate-status")
    suspend fun getNotificationsNegotiateResult(): NegotiationResponse

//    @GET("sliders/{slug}")
//    suspend fun getSliderCategoryItem(
//        @Path("slug") slug: String
//    ): NewsResponse

    @POST("livestocks/store-free")
    @FormUrlEncoded
    suspend fun storeTernakFree(
        @Field("kandang_id") kandang_id: Int,
        @Field("pakan") pakan_id: String,
        @Field("limbah_id") limbah_id: Int,
        @Field("gender") gender: String,
        @Field("age") age: String,
        @Field("type_id") type_id: Int,
        @Field("nominal") nominal: Int,
        @Field("status") status: String,
    ): StoreTernakFreeResponse
}
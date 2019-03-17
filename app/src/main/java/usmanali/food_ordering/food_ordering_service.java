package usmanali.food_ordering;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface food_ordering_service {
    @FormUrlEncoded
      @POST("get_products_by_catorgery.php")
    Call<ArrayList<Food>> getproductsbycatorgery(@Field("food_catorgery")String catorgery);
      @FormUrlEncoded
      @POST("Signup.php")
      Call<String> signup(@Field("name")String Name,@Field("password")String Password,@Field("phone")String Phone,@Field("address") String address,@Field("email") String email);
    @FormUrlEncoded
      @POST("insert_products_to_db.php")
      Call<String> insertproducts(@Field("food_name")String productname,@Field("food_price") String price,@Field("food_catorgery")String catorgery,@Field("food_image")String image,@Field("food_quantity") String quantity,@Field("food_weight") String weight);
    @FormUrlEncoded
      @POST("customerlogin.php")
      Call<ArrayList<Users>> login(@Field("email")String Username,@Field("password")String Password);
    @FormUrlEncoded
      @POST("increment_quantity.php")
      Call<String>increment_quantity(@Field("food_quantity")String quantity,@Field("food_id")String productid);
      @FormUrlEncoded
      @POST("decrement_quantity.php")
      Call<String>decrement_quantity(@Field("food_quantity")String quantity,@Field("food_id")String productid);
    @FormUrlEncoded
      @POST("place_orders.php")
      Call<String> place_orders(@Field("Name")String name,@Field("Phone")String phone,@Field("Address")String address,@Field("Email")String email,@Field("items")String itemname,@Field("Price")String price,@Field("DateTime")String DateTime);
    @FormUrlEncoded
      @POST("track_orders.php")
      Call<ArrayList<Orders>> track_orders(@Field("Username")String Username);
    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<ArrayList<Users>> forgot_password(@Field("email")String email);
    @GET("get_all_orders.php")
    Call<ArrayList<Orders>> get_all_orders();
    @FormUrlEncoded
    @POST("edit_food_detail.php")
    Call<String> edit_food_detail(@Field("food_name") String food_name,@Field("food_price")String food_price,@Field("food_weight")String weight,@Field("food_quantity")String food_quantity,@Field("food_id")String food_id);
    @FormUrlEncoded
    @POST("change_order_status.php")
    Call<String> change_status(@Field("order_id")String id,@Field("order_status")String status);
}

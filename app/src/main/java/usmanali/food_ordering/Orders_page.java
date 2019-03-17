package usmanali.food_ordering;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Orders_page extends AppCompatActivity {
RecyclerView orders_list;
SharedPreferences prefs;
    Users u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_page);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         u=new Gson().fromJson(prefs.getString("userinfo",""),Users.class);
        orders_list=findViewById(R.id.orders_list);
        orders_list.setLayoutManager(new LinearLayoutManager(this));
        if(u.user_role.equals("Customer")) {
            show_orders();
        }else if(u.user_role.equals("Admin")){
            show_all_orders();
        }
    }
  private void show_orders(){
      final android.app.AlertDialog waiting_dialog=new SpotsDialog(Orders_page.this);
      waiting_dialog.show();
      waiting_dialog.setMessage("Please Wait...");
        food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
      Call<ArrayList<Orders>> call=service.track_orders(u.email);
      call.enqueue(new Callback<ArrayList<Orders>>() {
          @Override
          public void onResponse(Call<ArrayList<Orders>> call, Response<ArrayList<Orders>> response) {
              waiting_dialog.dismiss();
              ArrayList<Orders> orders=response.body();
              if(orders!=null&&orders.size()>0)
              orders_list.setAdapter(new orders_list_adapter(orders,Orders_page.this));
          }

          @Override
          public void onFailure(Call<ArrayList<Orders>> call, Throwable t) {
              waiting_dialog.dismiss();
              Log.e("show_orders",t.getMessage());
          }
      });
  }
  private void show_all_orders(){
      final android.app.AlertDialog waiting_dialog=new SpotsDialog(Orders_page.this);
      waiting_dialog.show();
      waiting_dialog.setMessage("Please Wait...");
      food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
      Call<ArrayList<Orders>> call=service.get_all_orders();
      call.enqueue(new Callback<ArrayList<Orders>>() {
          @Override
          public void onResponse(Call<ArrayList<Orders>> call, Response<ArrayList<Orders>> response) {
              waiting_dialog.dismiss();
              ArrayList<Orders> orders=response.body();
              if(orders!=null&&orders.size()>0)
                  orders_list.setAdapter(new orders_list_adapter(orders,Orders_page.this));
          }

          @Override
          public void onFailure(Call<ArrayList<Orders>> call, Throwable t) {
              waiting_dialog.dismiss();
              Log.e("show_all_orders",t.getMessage());
          }
      });
  }
}

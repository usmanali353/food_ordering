package usmanali.food_ordering;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class food_list_page extends AppCompatActivity {
RecyclerView food_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("Menu"));
        food_list=findViewById(R.id.food_list);
        food_list.setLayoutManager(new LinearLayoutManager(this));
        fetch_food_by_catorgery(getIntent().getStringExtra("Menu"));
    }
  private void fetch_food_by_catorgery(String catorgery){
      final android.app.AlertDialog waiting_dialog=new SpotsDialog(food_list_page.this);
      waiting_dialog.show();
      waiting_dialog.setMessage("Please Wait...");
        food_ordering_service service =apiclient.getClient().create(food_ordering_service.class);
      Call<ArrayList<Food>> call=service.getproductsbycatorgery(catorgery);
      call.enqueue(new Callback<ArrayList<Food>>() {
          @Override
          public void onResponse(Call<ArrayList<Food>> call, Response<ArrayList<Food>> response) {
              waiting_dialog.dismiss();
              ArrayList<Food> foods=response.body();
              if(foods!=null&&foods.size()>0)
              food_list.setAdapter(new food_adapter(foods,food_list_page.this));
          }

          @Override
          public void onFailure(Call<ArrayList<Food>> call, Throwable t) {
              waiting_dialog.dismiss();
            Log.e("food_catorgery",t.getMessage());
          }
      });
  }
}

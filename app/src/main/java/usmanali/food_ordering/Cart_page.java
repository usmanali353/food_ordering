package usmanali.food_ordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_page extends AppCompatActivity {
RecyclerView cart_list;
Button btn_order_now;
dbhelper db;
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String user_info=prefs.getString("userinfo","");
        final Users users=new Gson().fromJson(user_info,Users.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cart_list=findViewById(R.id.cart_list);
        cart_list.setLayoutManager(new LinearLayoutManager(this));
        btn_order_now=findViewById(R.id.btn_order_now);
        db=new dbhelper(this);
        if(db.get_num_of_rows(users.email)>0){
           cart_list.setAdapter(new shopping_cart_adapter(show_products_in_cart(db,users.email),this));
        }else{
            Toast.makeText(Cart_page.this,"No Items in Cart",Toast.LENGTH_LONG).show();
        }
        btn_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.get_num_of_rows(users.email)>0){
                    startActivity(new Intent(Cart_page.this,Billing_Activity.class));
                }else{
                    Toast.makeText(Cart_page.this,"No Items in Cart",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public ArrayList<Food> show_products_in_cart(dbhelper mydb,String Username){
                  ArrayList<Food> productsincart=new ArrayList<>();
                  Cursor res = mydb.get_products_in_cart(Username);
                  if (res.getCount() == 0) {
                          Toast.makeText(Cart_page.this,"No Products in Cart",Toast.LENGTH_LONG) .show();
                      }
                  while (res.moveToNext()) {
                          Food p=new Food(res.getString(1),res.getInt(2),res.getInt(0),res.getString(3),res.getInt(4),res.getInt(6));
                          productsincart.add(p);
                      }
                  return productsincart;
              }

}

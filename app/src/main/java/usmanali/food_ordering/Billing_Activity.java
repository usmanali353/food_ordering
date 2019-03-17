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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Billing_Activity extends AppCompatActivity {
RecyclerView check_out_item_list;
SharedPreferences prefs;
Button placeorderbtn;
    Users u;
TextView order_item_count,order_full_amount,order_total_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        placeorderbtn=findViewById(R.id.placeorderbtn);
        order_item_count=findViewById(R.id.order_item_count);
        order_full_amount=findViewById(R.id.order_full_amounts);
        order_total_amount=findViewById(R.id.order_total_amount);
         u=new Gson().fromJson(prefs.getString("userinfo",""),Users.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        order_item_count.setText(String.valueOf(new dbhelper(this).get_num_of_rows(u.email)));
        order_total_amount.setText("Rs "+new dbhelper(this).getTotalOfAmount(u.email));
        order_full_amount.setText("Rs "+new dbhelper(this).getTotalOfAmount(u.email));
        check_out_item_list=findViewById(R.id.checkout_items_list);
        place_order();
        check_out_item_list.setLayoutManager(new LinearLayoutManager(this));
        check_out_item_list.setAdapter(new bill_adapter(show_products(new dbhelper(this),u.email)));
    }
    public ArrayList<Food> show_products(dbhelper mydb, String Username){
        ArrayList<Food> productsincart=new ArrayList<>();
        Cursor res = mydb.get_products_in_cart(Username);
        if (res.getCount() == 0) {
            Toast.makeText(Billing_Activity.this,"No Products",Toast.LENGTH_LONG) .show();
        }
        while (res.moveToNext()) {
            Food p=new Food(res.getString(1),res.getInt(2),res.getInt(0),res.getString(3),res.getInt(4),res.getInt(6));
            productsincart.add(p);
        }
        return productsincart;
    }
    private void place_order(){
        placeorderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final android.app.AlertDialog waiting_dialog=new SpotsDialog(Billing_Activity.this);
                waiting_dialog.show();
                waiting_dialog.setMessage("Please Wait...");
                Cursor res = new dbhelper(Billing_Activity.this).get_products_in_cart(u.email);
                if (res.getCount() == 0) {
                    Toast.makeText(Billing_Activity.this, "You dont have any items in cart", Toast.LENGTH_LONG).show();
                }else {
                    StringBuffer sb = new StringBuffer();
                    while (res.moveToNext()) {
                        sb.append(res.getString(1) + " ");
                        sb.append("x" + res.getInt(4) + " " + "\n");
                    }
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    food_ordering_service service = apiclient.getClient().create(food_ordering_service.class);
                    Call<String> call=service.place_orders(u.name,u.phone,u.address,u.email,sb.toString(),String.valueOf(new dbhelper(Billing_Activity.this).getTotalOfAmount(u.email)),mydate);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            waiting_dialog.dismiss();
                           Toast.makeText(Billing_Activity.this,"Order Placed Sucessfully",Toast.LENGTH_LONG).show();
                           new dbhelper(Billing_Activity.this).delete_all(u.email);
                           startActivity(new Intent(Billing_Activity.this,menu_page.class));
                           finish();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            waiting_dialog.dismiss();
                            Log.e("place_orders",t.getMessage());
                        }
                    });
                }
            }
        });
    }
}

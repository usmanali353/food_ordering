package usmanali.food_ordering;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class order_details extends AppCompatActivity {
TextView orderid,name,price,email,address,phone,items,orderdatetime;
Button change_status;
    String[] listitems={"Departed","Delivered"};
    SharedPreferences prefs;
    String current_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        prefs=PreferenceManager.getDefaultSharedPreferences(order_details.this);
        Users u=new Gson().fromJson(prefs.getString("userinfo",""),Users.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Orders order_info=new Gson().fromJson(getIntent().getStringExtra("order_info"),Orders.class);
        orderid=findViewById(R.id.orderid);
        name=findViewById(R.id.name);
        price=findViewById(R.id.price);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.phone);
        items=findViewById(R.id.items);
        orderdatetime=findViewById(R.id.orderdatetime);
        orderid.setText(String.valueOf(order_info.order_id));
        name.setText(order_info.Name);
        price.setText("Rs "+order_info.price);
        email.setText(order_info.Email);
        address.setText(order_info.Address);
        phone.setText(order_info.Phone);
        items.setText(order_info.items);
        orderdatetime.setText(order_info.orderdatetime);
        change_status=findViewById(R.id.change_status);
        if(u.user_role.equals("Admin")){
            change_status.setVisibility(View.VISIBLE);
            change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder status_dialog=new AlertDialog.Builder(order_details.this);
                    status_dialog.setTitle("Select Status For Order");
                    status_dialog.setSingleChoiceItems(listitems, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                          current_item=listitems[which];
                        }
                    }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(current_item!=null){
                                final android.app.AlertDialog waiting_dialog=new SpotsDialog(order_details.this);
                                waiting_dialog.show();
                                waiting_dialog.setMessage("Please Wait...");
                                food_ordering_service service =apiclient.getClient().create(food_ordering_service.class);
                                Call<String> call=service.change_status(String.valueOf(order_info.order_id),current_item);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        waiting_dialog.dismiss();
                                        Toast.makeText(order_details.this,response.body(),Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                         waiting_dialog.dismiss();
                                        Log.e("change_status",t.getMessage());
                                    }
                                });
                            }else{
                                Toast.makeText(order_details.this,"Select Status",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }else if(u.user_role.equals("Customer")){
            change_status.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            startActivity(new Intent(order_details.this,Orders_page.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

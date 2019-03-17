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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class food_detail extends AppCompatActivity {
ImageView food_image;
TextView name,price,weight,productid,productcatorgery;
dbhelper db;
NumberPicker quantity_selector;
SharedPreferences prefs;
    Food f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Users user=new Gson().fromJson(prefs.getString("userinfo", ""),Users.class);
         f=new Gson().fromJson(getIntent().getStringExtra("food_detail"),Food.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(f.food_name);
        food_image=findViewById(R.id.productimage);
        Picasso.get().load(f.food_image).into(food_image);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        db=new dbhelper(this);
        name=findViewById(R.id.name);
        name.setText(f.food_name);
        price=findViewById(R.id.price);
        price.setText("Rs "+f.food_price);
        weight=findViewById(R.id.weight);
        weight.setText(f.food_weight);
        productid=findViewById(R.id.productid);
        productid.setText(String.valueOf(f.food_id));
        productcatorgery=findViewById(R.id.catorgery);
        productcatorgery.setText(f.food_catorgery);
        if(!prefs.getBoolean("IsLogin",false)){
            fab.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(prefs.getBoolean("IsLogin",false)) {
                    AlertDialog.Builder select_quantity = new AlertDialog.Builder(food_detail.this);
                    select_quantity.setTitle("Select Quantity");
                    View v = LayoutInflater.from(food_detail.this).inflate(R.layout.alertdialoglayout, null);
                    quantity_selector = v.findViewById(R.id.selectquantity);
                    quantity_selector.setMaxValue(f.food_quantity);
                    quantity_selector.setMinValue(1);
                    select_quantity.setView(v);
                    select_quantity.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Boolean inserted = db.insert_product_toshoppingcart(f.food_name, f.food_price*quantity_selector.getValue(), f.food_image, quantity_selector.getValue(),user.email, f.food_id);
                            if (inserted) {
                                decrement_quantity(String.valueOf(quantity_selector.getValue()),String.valueOf(f.food_id),quantity_selector);
                                quantity_selector.setMaxValue(quantity_selector.getMaxValue()-quantity_selector.getValue());
                            } else {
                                Toast.makeText(food_detail.this, "Product not Added to Cart", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }else{
                    Toast.makeText(food_detail.this,"Please Sign In First",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void decrement_quantity(String quantity, String food_id, final NumberPicker quantity_selector){
        final android.app.AlertDialog waiting_dialog=new SpotsDialog(food_detail.this);
        waiting_dialog.show();
        waiting_dialog.setMessage("Please Wait...");
        food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
        Call<String> call=service.decrement_quantity(quantity,food_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                waiting_dialog.dismiss();
                Toast.makeText(food_detail.this,response.body(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                waiting_dialog.dismiss();
                Log.e("decrement_quantity",t.getMessage());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            startActivity(new Intent(food_detail.this,food_list_page.class).putExtra("Menu",f.food_catorgery));
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}

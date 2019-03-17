package usmanali.food_ordering;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_food_detail extends AppCompatActivity {
 TextInputEditText food_id,food_name,food_quantity,food_price,food_weight;
  Button save_changes;
    Food f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        food_id=findViewById(R.id.food_id_txt);
        food_name=findViewById(R.id.food_name_txt);
        food_quantity=findViewById(R.id.food_quantity_txt);
        food_price=findViewById(R.id.food_price_txt);
        food_weight=findViewById(R.id.food_weight_txt);
        save_changes=findViewById(R.id.edit_details_btn);
         f=new Gson().fromJson(getIntent().getStringExtra("food_detail"),Food.class);
        food_id.setText(String.valueOf(f.food_id));
        food_name.setText(f.food_name);
        food_quantity.setText(String.valueOf(f.food_quantity));
        food_price.setText(String.valueOf(f.food_price));
        food_weight.setText(f.food_weight);
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food_name.getText().toString().isEmpty()){
                    food_name.setError("Enter Food Name");
                }else if(food_weight.getText().toString().isEmpty()){
                    food_weight.setError("Enter Food Weight");
                }else if(food_price.getText().toString().isEmpty()||food_price.getText().toString().startsWith("0")){
                    food_price.setError("Enter Food Price");
                }else if(food_quantity.getText().toString().isEmpty()||food_quantity.getText().toString().startsWith("0")) {
                    food_quantity.setError("Enter Food Quantity");
                }else {
                    final AlertDialog waiting_dialog=new SpotsDialog(Edit_food_detail.this);
                    waiting_dialog.show();
                    waiting_dialog.setMessage("Please Wait...");
                    food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
                    Call<String> call=service.edit_food_detail(food_name.getText().toString(),food_price.getText().toString(),food_weight.getText().toString(),food_quantity.getText().toString(),food_id.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            waiting_dialog.dismiss();
                            Toast.makeText(Edit_food_detail.this,response.body(),Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            waiting_dialog.dismiss();
                            Log.e("Edit_food",t.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
           startActivity(new Intent(Edit_food_detail.this,food_list_page.class).putExtra("Menu",f.food_catorgery));
           finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

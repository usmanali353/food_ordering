package usmanali.food_ordering;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_food extends AppCompatActivity {
EditText food_name,food_quantity,food_price,food_weight;
Spinner food_catorgery;
Button add;
Bitmap bitmap=null;
    private Uri filepath;
    private int PICK_IMAGE_REQUEST = 100;
ImageView food_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        food_name=findViewById(R.id.productnametxt);
        food_quantity=findViewById(R.id.productquantitytxt);
        food_weight=findViewById(R.id.productweighttxt);
        food_price=findViewById(R.id.pricetxt);
        add=findViewById(R.id.upload_btn);
        food_image=findViewById(R.id.productimg);
        food_catorgery=findViewById(R.id.productcatorgeryselector);
        add_image();
        add_product();
    }

    private void add_image(){
        food_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                          i.setType("image/*");
                          i.setAction(Intent.ACTION_GET_CONTENT);
                          startActivityForResult(i, PICK_IMAGE_REQUEST);

            }
        });
    }
    @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                  super.onActivityResult(requestCode, resultCode, data);
                  if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData()!=null) {
                          filepath=data.getData();
                          try {
                                  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                                  food_image.setImageBitmap(bitmap);
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                      }
              }

    private void add_product(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food_name.getText().toString().isEmpty()){
                    food_name.setError("Enter Food Name");
                }else if(food_catorgery.getSelectedItem().toString().equals("Select Catorgery")){
                    Toast.makeText(Add_food.this,"Sekect Catorgery",Toast.LENGTH_LONG).show();
                }else if(food_weight.getText().toString().isEmpty()){
                    food_weight.setError("Enter Food Weight");
                }else if(food_price.getText().toString().isEmpty()||food_price.getText().toString().startsWith("0")){
                    food_price.setError("Enter Food Price");
                }else if(food_quantity.getText().toString().isEmpty()||food_quantity.getText().toString().startsWith("0")) {
                    food_quantity.setError("Enter Food Quantity");
                }else{
                    final android.app.AlertDialog waiting_dialog=new SpotsDialog(Add_food.this);
                    waiting_dialog.show();
                    waiting_dialog.setMessage("Please Wait...");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Log.e("image",encodedImage);
                    food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
                    Call<String> call=service.insertproducts(food_name.getText().toString(),food_price.getText().toString(),food_catorgery.getSelectedItem().toString(),encodedImage,food_quantity.getText().toString(),food_weight.getText().toString());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            waiting_dialog.dismiss();
                            Toast.makeText(Add_food.this,"Food Added",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            waiting_dialog.dismiss();
                            Log.e("add_food",t.getMessage());
                        }
                    });
                }
            }
        });
}
}

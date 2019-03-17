package usmanali.food_ordering;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  {
Button btnsignin,btnRegister;
TextView forgot_password;
MaterialEditText register_email;
    MaterialEditText register_password;
    MaterialEditText register_address;

    MaterialEditText register_phone;

    MaterialEditText register_name;
 Gson g;
 SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024,1024);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        pref=PreferenceManager.getDefaultSharedPreferences(this);
        g=new Gson();
        btnsignin=findViewById(R.id.btnSignin);
        forgot_password=findViewById(R.id.txt_forgot_password);
        btnRegister=findViewById(R.id.btnRegister);
        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_up();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot_password();
            }
        });
    }
private void login(){
    View v= LayoutInflater.from(this).inflate(R.layout.layout_login,null);
    final MaterialEditText email=v.findViewById(R.id.emailtxt);
    final MaterialEditText password=v.findViewById(R.id.passwordtxt);
    AlertDialog.Builder login_dialog=new AlertDialog.Builder(this)
    .setTitle("Sign In")
    .setMessage("Use Email and Password to Sign in")
            .setCancelable(false)
    .setView(v)
    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    }).setNeutralButton("Use as Guest", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    });
    AlertDialog dialog =login_dialog.create();
    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
        @Override
        public void onShow(final DialogInterface dialog) {
            Button login = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            login.setText("Sign In");
            Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
            cancel.setText("Cancel");
            Button Guest = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
            Guest.setText("Use as Guest");
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(email.getText().toString().isEmpty()){
                        email.setError("Enter Email");
                    }else if(password.getText().toString().isEmpty()){
                        password.setError("Enter Password");
                    }else if(password.getText().toString().length()<6){
                        password.setError("Password too short");
                    } else if (!isEmailValid(email.getText().toString())) {
                        email.setError("Invalid Email");
                    }else {
                        final android.app.AlertDialog waiting_dialog=new SpotsDialog(MainActivity.this);
                        waiting_dialog.show();
                        waiting_dialog.setMessage("Please Wait...");
                        food_ordering_service service =apiclient.getClient().create(food_ordering_service.class);
                      Call<ArrayList<Users>> call=service.login(email.getText().toString(),password.getText().toString());
                      call.enqueue(new Callback<ArrayList<Users>>() {
                          @Override
                          public void onResponse(Call<ArrayList<Users>> call, Response<ArrayList<Users>> response) {
                              waiting_dialog.dismiss();
                              ArrayList<Users> user_info=response.body();
                              if(user_info!=null)
                              if(user_info.get(0).status.equals("Login Sucess")){
                                  Intent i=new Intent(MainActivity.this,menu_page.class);
                                String user_information= g.toJson(user_info.get(0));
                                  pref.edit().putString("userinfo",user_information).apply();
                                  pref.edit().putBoolean("IsLogin",true).apply();
                                  Toast.makeText(MainActivity.this, "Welcome " + user_info.get(0).name, Toast.LENGTH_LONG).show();
                                  startActivity(i);
                                  finish();
                              }else {
                                  Toast.makeText(MainActivity.this,user_info.get(0).status,Toast.LENGTH_LONG).show();
                              }
                          }

                          @Override
                          public void onFailure(Call<ArrayList<Users>> call, Throwable t) {
                              waiting_dialog.dismiss();
                             Log.e("login_error",t.getMessage());
                          }
                      });
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Guest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,menu_page.class));
                    finish();
                }
            });
        }
    });
    dialog.show();
}
    private void sign_up(){
        View v= LayoutInflater.from(this).inflate(R.layout.layout_register,null);
        register_email=v.findViewById(R.id.emailtxt);
        register_password=v.findViewById(R.id.passwordtxt);
        register_address=v.findViewById(R.id.address);
        register_phone=v.findViewById(R.id.phone);
        register_name=v.findViewById(R.id.nametxt);
        final AlertDialog.Builder register_dialog=new AlertDialog.Builder(this)
       .setTitle("Register")
       .setMessage("Please provide all required fields")
       .setCancelable(false)
       .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
        .setView(v);

       AlertDialog dialog= register_dialog.create();
       dialog.setOnShowListener(new DialogInterface.OnShowListener() {
           @Override
           public void onShow(final DialogInterface dialog) {
               Button sign_up = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
               sign_up.setText("Sign Up");
               Button cancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
               cancel.setText("Cancel");
               cancel.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       dialog.dismiss();
                   }
               });
               sign_up.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(register_name.getText().toString().isEmpty()){
                           register_name.setError("Enter Name");
                       }else if(register_email.getText().toString().isEmpty()){
                           register_email.setError("Enter Email");
                       }else if(register_address.getText().toString().isEmpty()){
                           register_address.setError("Enter Address");
                       }else if(register_password.getText().toString().isEmpty()){
                           register_password.setError("Enter Password");
                       }else if(register_phone.getText().toString().isEmpty()){
                           register_phone.setError("Enter Phone");
                       }else if(register_password.getText().toString().length()<6){
                           register_password.setError("Password too Short");
                       } else if (!isEmailValid(register_email.getText().toString())) {
                           register_email.setError("Invalid Email");
                       }else{
                           final android.app.AlertDialog waiting_dialog=new SpotsDialog(MainActivity.this);
                           waiting_dialog.show();
                           waiting_dialog.setMessage("Please Wait...");
                           food_ordering_service service =apiclient.getClient().create(food_ordering_service.class);
                           Call<String> call=service.signup(register_name.getText().toString(),register_password.getText().toString(),register_phone.getText().toString(),register_address.getText().toString(),register_email.getText().toString());
                           call.enqueue(new Callback<String>() {
                               @Override
                               public void onResponse(Call<String> call, Response<String> response) {
                                   waiting_dialog.dismiss();
                                   Toast.makeText(MainActivity.this,response.body(),Toast.LENGTH_LONG).show();
                                   dialog.dismiss();
                               }

                               @Override
                               public void onFailure(Call<String> call, Throwable t) {
                                   waiting_dialog.dismiss();
                                   Log.e("register_error",t.getMessage());
                               }
                           });
                   }}
               });

           }
       });
        dialog.show();
    }
    private void forgot_password(){
        AlertDialog.Builder forgot_password_dialog=new AlertDialog.Builder(this);
        forgot_password_dialog.setTitle("Forgot Password");
        forgot_password_dialog.setMessage("Use Email to Recover Password");
        View v=LayoutInflater.from(this).inflate(R.layout.forgot_password_layout,null);
        final MaterialEditText email=v.findViewById(R.id.emailtxt);
        forgot_password_dialog.setView(v);
        forgot_password_dialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(email.getText().toString().isEmpty()){
                    email.setError("Enter Email");
                } else if (!isEmailValid(email.getText().toString())) {
                    email.setError("Invalid Email");
                } else {
                    final android.app.AlertDialog waiting_dialog = new SpotsDialog(MainActivity.this);
                    waiting_dialog.show();
                    waiting_dialog.setMessage("Please Wait...");
                    food_ordering_service service = apiclient.getClient().create(food_ordering_service.class);
                    Call<ArrayList<Users>> call = service.forgot_password(email.getText().toString());
                    call.enqueue(new Callback<ArrayList<Users>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Users>> call, Response<ArrayList<Users>> response) {
                            waiting_dialog.dismiss();
                            new sendmail(MainActivity.this, email.getText().toString(), "Password Recover Request", "Your Password is " + response.body().get(0).password + "\n" + "Keep this Password Safe").execute();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Users>> call, Throwable t) {
                            waiting_dialog.dismiss();
                            Log.e("forgot_password", t.getMessage());
                        }
                    });
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    public  boolean isEmailValid(String email) {
                  String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                  Matcher matcher = pattern.matcher(email);
                  return matcher.matches();
    }


}

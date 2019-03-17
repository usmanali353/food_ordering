package usmanali.food_ordering;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class menu_page extends AppCompatActivity {
    ArrayList<Menu> menuArrayList;
    RecyclerView menu_list;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerlayout;
    NavigationView nv;
    Gson g;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");
        g=new Gson();
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        nv=findViewById(R.id.nav_view);
        if(prefs.getBoolean("IsLogin", false)) {
            Users user_info = g.fromJson(prefs.getString("userinfo",""), Users.class);
            if(user_info.user_role.equals("Admin")){
                nv.inflateMenu(R.menu.nav_drawer_menu_admin);
            }else if(user_info.user_role.equals("Customer")){
                nv.inflateMenu(R.menu.nav_drawaer_menu);
            }
        }else{
            nv.inflateMenu(R.menu.nav_drawer_menu_guest);
        }

        drawerlayout=findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerlayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        menuArrayList = new ArrayList<>();
        //add menus
        menuArrayList.add(new Menu("Hot Drinks",R.drawable.sicak_icecek));
        menuArrayList.add(new Menu("Cold Drinks",R.drawable.soguk_icecek));
        menuArrayList.add(new Menu("Breakfast",R.drawable.kahvalti));
        menuArrayList.add(new Menu("Snacks",R.drawable.aperatifler));
        menuArrayList.add(new Menu("Main Course",R.drawable.anayemekler));
        menu_list=findViewById(R.id.menu_list);
        menu_list.setLayoutManager(new LinearLayoutManager(this));
        menu_list.setAdapter(new menu_adapter(menuArrayList,this));

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.cart){
                    if(!prefs.getBoolean("IsLogin",false)){
                        Toast.makeText(menu_page.this,"Please Sign In to access the Cart",Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(new Intent(menu_page.this, Cart_page.class));
                    }
                }else if(menuItem.getItemId()==R.id.orders){
                    if(!prefs.getBoolean("IsLogin",false)){
                        Toast.makeText(menu_page.this,"Please Sign In to View Your Orders",Toast.LENGTH_LONG).show();
                    }else {
                        startActivity(new Intent(menu_page.this, Orders_page.class));
                    }
                }else if(menuItem.getItemId()==R.id.log_out){
                    prefs.edit().remove("IsLogin").apply();
                    prefs.edit().remove("userinfo").apply();
                    startActivity(new Intent(menu_page.this,MainActivity.class));
                    finish();
                }else if(menuItem.getItemId()==R.id.add_food){
                    startActivity(new Intent(menu_page.this,Add_food.class));
                }else if(menuItem.getItemId()==R.id.all_orders){
                    startActivity(new Intent(menu_page.this,Orders_page.class));
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        prefs.edit().remove("IsLogin").apply();
        prefs.edit().remove("userinfo").apply();
        finish();
    }
}

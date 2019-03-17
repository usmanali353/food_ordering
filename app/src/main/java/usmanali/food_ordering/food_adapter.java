package usmanali.food_ordering;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class food_adapter extends RecyclerView.Adapter<food_adapter.food_list_viewholder> {
    ArrayList<Food> foods;
    Context context;
    SharedPreferences prefs;
    Users u;
    public food_adapter(ArrayList<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
        prefs=PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public food_list_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.food_list_layout,viewGroup,false);
        return new food_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull food_list_viewholder food_list_viewholder, final int i) {
         u=new Gson().fromJson(prefs.getString("userinfo",""),Users.class);
        Picasso.get().load(foods.get(i).food_image).into(food_list_viewholder.food_image);
        food_list_viewholder.food_name.setText(foods.get(i).food_name);
        food_list_viewholder.food_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u != null) {
                    if (u.user_role.equals("Admin")) {
                        String food_list = new Gson().toJson(foods.get(i));
                        Intent i = new Intent(context, Edit_food_detail.class);
                        i.putExtra("food_detail", food_list);
                        context.startActivity(i);
                    } else if (u.user_role.equals("Customer")) {
                        String food_list = new Gson().toJson(foods.get(i));
                        Intent i = new Intent(context, food_detail.class);
                        i.putExtra("food_detail", food_list);
                        context.startActivity(i);
                    }
                }else {
                    String food_list = new Gson().toJson(foods.get(i));
                    Intent i = new Intent(context, food_detail.class);
                    i.putExtra("food_detail", food_list);
                    context.startActivity(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class food_list_viewholder extends RecyclerView.ViewHolder{
          ImageView food_image;
          TextView  food_name;

        public food_list_viewholder(@NonNull View itemView) {
            super(itemView);
            food_image=itemView.findViewById(R.id.food_image);
            food_name=itemView.findViewById(R.id.food_name);
        }
    }

}

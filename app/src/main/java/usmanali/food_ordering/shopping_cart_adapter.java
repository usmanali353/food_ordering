package usmanali.food_ordering;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class shopping_cart_adapter extends RecyclerView.Adapter<shopping_cart_adapter.shopping_cart_viewholder> {
    ArrayList<Food> foods;
    Context context;

    public shopping_cart_adapter(ArrayList<Food> foods, Context context) {
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public shopping_cart_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shoppingcartlistlayout,viewGroup,false);
        return new shopping_cart_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final shopping_cart_viewholder shopping_cart_viewholder, final int i) {
        Picasso.get().load(foods.get(i).food_image).into(shopping_cart_viewholder.productImage);
        shopping_cart_viewholder.productPrice.setText("Rs "+String.valueOf(foods.get(i).food_price));
        shopping_cart_viewholder.productName.setText(foods.get(i).food_name);
        shopping_cart_viewholder.productquantity.setText("Quantity x"+String.valueOf(foods.get(i).food_quantity));
        shopping_cart_viewholder.excludefromcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer rows = new dbhelper(context).delete(String.valueOf(foods.get(i).id));
                                  if (rows > 0) {
                                          increment_quantity(String.valueOf(foods.get(i).food_quantity),String.valueOf(foods.get(i).food_id));
                                          foods.remove(shopping_cart_viewholder.getAdapterPosition());
                                          notifyItemRemoved(i);
                                          notifyItemRangeChanged(shopping_cart_viewholder.getAdapterPosition(),getItemCount());
                                          notifyDataSetChanged();
                                      }else{
                                          Toast.makeText(context,"Item not Removed From Cart",Toast.LENGTH_LONG).show();
                                      }

            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class shopping_cart_viewholder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,productquantity,productPrice;
        Button excludefromcart;
        public shopping_cart_viewholder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.productImage);
            productName=itemView.findViewById(R.id.productName);
            productquantity=itemView.findViewById(R.id.productquantity);
            productPrice=itemView.findViewById(R.id.productPrice);
            excludefromcart=itemView.findViewById(R.id.excludefromcart);
        }
    }
    private void increment_quantity(String quantity,String food_id){
        final android.app.AlertDialog waiting_dialog=new SpotsDialog(context);
        waiting_dialog.show();
        waiting_dialog.setMessage("Please Wait...");
        food_ordering_service service=apiclient.getClient().create(food_ordering_service.class);
        Call<String> call=service.increment_quantity(quantity,food_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                waiting_dialog.dismiss();
                Toast.makeText(context,response.body(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                waiting_dialog.dismiss();
                Log.e("increment_quantity",t.getMessage());
            }
        });
    }
}

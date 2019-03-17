package usmanali.food_ordering;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class bill_adapter extends RecyclerView.Adapter<bill_adapter.bill_viewholder> {
    ArrayList<Food> foods;

    public bill_adapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public bill_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchased_item_layout,viewGroup,false);
        return new bill_viewholder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull bill_viewholder bill_viewholder, int i) {
     bill_viewholder.checkout_item_name.setText(foods.get(i).food_name);
     bill_viewholder.checkout_item_quantity.setText("x"+foods.get(i).food_quantity);
     bill_viewholder.checkout_item_price.setText("Rs "+foods.get(i).food_price);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    class bill_viewholder extends RecyclerView.ViewHolder{
       TextView checkout_item_name,checkout_item_quantity,checkout_item_price;
        public bill_viewholder(@NonNull View itemView) {
            super(itemView);
            checkout_item_name=itemView.findViewById(R.id.checkout_item_name);
            checkout_item_quantity=itemView.findViewById(R.id.checkout_item_quantity);
            checkout_item_price=itemView.findViewById(R.id.checkout_item_price);
        }
    }
}

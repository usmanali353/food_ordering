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
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class orders_list_adapter extends RecyclerView.Adapter<orders_list_adapter.order_list_viewholder> {
    ArrayList<Orders> orders;
    Context context;
    SharedPreferences prefs;
    public orders_list_adapter(ArrayList<Orders> orders, Context context) {
        this.orders = orders;
        this.context = context;
        prefs=PreferenceManager.getDefaultSharedPreferences(context);
    }

    @NonNull
    @Override
    public order_list_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_layout,viewGroup,false);
        return new order_list_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull order_list_viewholder order_list_viewholder, final int i) {
        Users u=new Gson().fromJson(prefs.getString("userinfo",""),Users.class);
          order_list_viewholder.order_price.setText("Rs "+String.valueOf(orders.get(i).price));
          order_list_viewholder.order_id.setText("Order id: "+String.valueOf(orders.get(i).order_id));
          order_list_viewholder.order_date.setText(orders.get(i).orderdatetime);
          order_list_viewholder.order_status.setText(orders.get(i).order_status);
          order_list_viewholder.order_detail.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                context.startActivity(new Intent(context,order_details.class).putExtra("order_info",new Gson().toJson(orders.get(i))));
              }
          });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class order_list_viewholder extends RecyclerView.ViewHolder{
        TextView order_date,order_price,order_id,order_detail,order_status;
        public order_list_viewholder(@NonNull View itemView) {
            super(itemView);
            order_date=itemView.findViewById(R.id.order_date);
            order_id=itemView.findViewById(R.id.order_id);
            order_price=itemView.findViewById(R.id.order_price);
            order_detail=itemView.findViewById(R.id.order_detail);
            order_status=itemView.findViewById(R.id.order_status);
        }
    }
}

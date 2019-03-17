package usmanali.food_ordering;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class menu_adapter extends RecyclerView.Adapter<menu_adapter.menu_viewholder> {
    ArrayList<Menu> menus;
    Context c;
    public menu_adapter(ArrayList<Menu> menuArrayList, Context context){
        this.menus=menuArrayList;
        this.c=context;
    }
    @NonNull
    @Override
    public menu_viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_layout,viewGroup,false);
        return new menu_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final menu_viewholder menu_viewholder, int i) {
          final Menu m=menus.get(i);
          menu_viewholder.label.setBackgroundResource(m.picture);
          menu_viewholder.menu_name.setText(m.name);
          menu_viewholder.label.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  c.startActivity(new Intent(c,food_list_page.class).putExtra("Menu",m.name));
              }
          });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    class menu_viewholder extends RecyclerView.ViewHolder {
        RelativeLayout label;
        TextView menu_name;
        public menu_viewholder(@NonNull View itemView) {
            super(itemView);
            label=itemView.findViewById(R.id.label);
            menu_name=itemView.findViewById(R.id.menu_name);
        }
    }
}

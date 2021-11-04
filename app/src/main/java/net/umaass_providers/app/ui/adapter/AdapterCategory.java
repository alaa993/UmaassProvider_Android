package net.umaass_providers.app.ui.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.interfac.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {

    private Activity activity;
    private Context mContext;
    private List<Category> lists = new ArrayList<>();
    private ItemClickListener<Category> listener;
    private int lastSelectedPosition;
    private boolean choosingMode;

    public AdapterCategory() {
    }

    public AdapterCategory(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_category, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        //final Category item = lists.get(position);
        holder.bind(null);
//        holder.bind();
    }

    @Override
    public int getItemCount() {
        return /*lists.size()*/10;
    }

    public void clearAndPut(List<Category> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void set(List<Category> items) {
        lists = items;
        notifyDataSetChanged();
    }

    public void put(List<Category> items) {
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void enableChoosingMode() {
        choosingMode = true;
    }

    public Category getItem(int position) {
        return lists.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Category item;
        TextView txtTitle;
        ImageView imgIcon;

        MyViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            imgIcon = view.findViewById(R.id.imgIcon);
        }

        void bind(Category i) {
            item = i;
            /*CategoryView categoryView = (CategoryView) itemView;
            categoryView.setText(i.getName());
            categoryView.setTextSizeDp(12);

            if (choosingMode){
                if (i.isSelected()){
                    categoryView.setCellColor(R.color.colorPrimaryLight);
                    lastSelectedPosition =  lists.indexOf(i);
                    categoryView.setTextColor(Utils.getColor(R.color.white));
                }else{
                    categoryView.setCellColor(R.color.divider_color);
                    categoryView.setTextColor(Utils.getColor(R.color.colorPrimaryText));
                }
            }else {
                categoryView.setCellColor(R.color.colorPrimaryLight);
                categoryView.setTextColor(Utils.getColor(R.color.white));
            }*/
           /* if (choosingMode) {
                itemView.setBackground(i.isSelected() ?
                                       activity.getResources().getDrawable(R.drawable.round_btn_selected) :
                                       activity.getResources().getDrawable(R.drawable.round_btn));
                txtTitle.setTextColor(i.isSelected() ?
                                      activity.getResources().getColor(R.color.white) :
                                      activity.getResources().getColor(R.color.secondaryPrimaryText));
            }*/

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(item);

            /*if (choosingMode) {
                lists.get(lastSelectedPosition).setSelected(false);
                notifyItemChanged(lastSelectedPosition);

                lastSelectedPosition = lists.indexOf(item);
                lists.get(lastSelectedPosition).setSelected(true);
                notifyItemChanged(lastSelectedPosition);
            }*/
        }
    }


}
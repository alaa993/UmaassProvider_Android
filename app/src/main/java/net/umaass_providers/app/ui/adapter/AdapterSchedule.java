package net.umaass_providers.app.ui.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.Category;
import net.umaass_providers.app.interfac.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class AdapterSchedule extends RecyclerView.Adapter<AdapterSchedule.MyViewHolder> {

    private Context mContext;
    private List<Category> lists = new ArrayList<>();
    private ItemClickListener<Category> listener;
    private ItemClickListener<Category> bookLitener;
    private int lastSelectedPosition;
    private boolean choosingMode;

    public AdapterSchedule() {
    }

    public void setListener(ItemClickListener<Category> listener) {
        this.listener = listener;
    }

    public void setBookLitener(ItemClickListener<Category> bookLitener) {
        this.bookLitener = bookLitener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_schedule, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // final Category item = lists.get(position);
        holder.bind(null);
    }

    @Override
    public int getItemCount() {
        return /*lists == null ? 0 : lists.size()*/ 15;
    }

    public void clearAndPut(List<Category> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }

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

        MyViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
        }

        void bind(Category i) {
            item = i;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                if (listener != null) {
                    listener.onClick(item);
                }
            }
        }
    }


}
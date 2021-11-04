package net.umaass_providers.app.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.ServicesItem;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AdapterService extends RecyclerView.Adapter<AdapterService.MyViewHolder> {

    private List<ServicesItem> lists = new ArrayList<>();
    private ItemClickListener<ServicesItem> listener;
    private ItemClickListener<ServicesItem> deleteListener;
    private ItemClickListener<ServicesItem> editListener;

    public void setListener(ItemClickListener<ServicesItem> listener) {
        this.listener = listener;
    }

    public void setDeleteListener(ItemClickListener<ServicesItem> deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setEditListener(ItemClickListener<ServicesItem> editListener) {
        this.editListener = editListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_service, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        ServicesItem item = lists.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public void clearAndPut(List<ServicesItem> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }

        notifyDataSetChanged();
    }

    public void removeItem(ServicesItem item) {
        int pos = lists.indexOf(item);
        lists.remove(item);
        notifyItemRangeRemoved(pos, lists.size());
    }

    public void put(List<ServicesItem> items) {
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }


    public ServicesItem getItem(int position) {
        return lists.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ServicesItem item;
        TextView txtName;
        TextView txtPrice;
        TextView txtDuration;
        TextView txtTypePrice;
        TextView txtTypeDuration;
        TextView txtDesc;
        TextView btnEdit;
        TextView btnDelete;

        MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtDuration = view.findViewById(R.id.txtDuration);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtTypePrice = view.findViewById(R.id.txtTypePrice);
            txtTypeDuration = view.findViewById(R.id.txtTypeDuration);
            btnEdit = view.findViewById(R.id.btnEdit);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

        void bind(ServicesItem i) {
            item = i;
            txtName.setText(i.getTitle());
            txtPrice.setText(Utils.getString(R.string.price_));
            txtPrice.append(String.valueOf(i.getPrice()));
            txtTypePrice.setText(i.getTypePrice());
            txtDesc.setText(i.getNotesForTheCustomer());
            txtDuration.setText(Utils.getString(R.string.duration_));
            txtDuration.append(String.valueOf(i.getDuration()));
            txtTypeDuration.setText(i.getTypeTime());
            itemView.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            Log.i("hyhyhyhyhy", i.getNotesForTheCustomer().toString());

        }


        @Override
        public void onClick(View view) {
            if (view == itemView) {
                if (listener != null) {
                    listener.onClick(item);
                }
            } else if (view == btnEdit) {
                if (editListener != null) {
                    editListener.onClick(item);
                }

            } else if (view == btnDelete) {
                if (deleteListener != null) {
                    deleteListener.onClick(item);
                }
            }


        }

    }


}
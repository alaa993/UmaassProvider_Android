package net.umaass_providers.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.G;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.models.CustomerFile;

import java.util.ArrayList;
import java.util.List;


public class AdapterCustomerFiles extends RecyclerView.Adapter<AdapterCustomerFiles.MyViewHolder> {

    private Context mContext;
    private List<CustomerFile> lists = new ArrayList<>();
    private ItemClickListener<CustomerFile> listener;
    private ItemClickListener<CustomerFile> deleteListener;
    private int lastSelectedPosition;
    private boolean choosingMode;

    public AdapterCustomerFiles() {
    }

    public void setListener(ItemClickListener<CustomerFile> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer_files, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        CustomerFile item = lists.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public void clearAndPut(List<CustomerFile> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void put(List<CustomerFile> items) {
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void setDeleteListener(ItemClickListener<CustomerFile> deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void enableChoosingMode() {
        choosingMode = true;
    }

    public CustomerFile getItem(int position) {
        return lists.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CustomerFile item;
        TextView txtDate;
        TextView txtTypeService;
        TextView btnEdit;
        TextView txtPrescription;
        RecyclerView recyclerView;

        MyViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerView);
            txtDate = view.findViewById(R.id.txtDate);
            txtTypeService = view.findViewById(R.id.txtTypeService);
            btnEdit = view.findViewById(R.id.btnEdit);
            txtPrescription = view.findViewById(R.id.txtPrescription);
        }

        void bind(CustomerFile i) {
            item = i;
            itemView.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
            txtPrescription.setText(item.appointment.getPrescription());
            txtDate.setText(item.appointment.getFromTo());
            txtTypeService.setText(item.appointment.getService().getTitle());
            recyclerView.setAdapter(item.adapterGalleryFiles);
            recyclerView.setLayoutManager(new LinearLayoutManager(G.getInstance(), RecyclerView.HORIZONTAL, false));
            //   itemView.setBackgroundColor(Utils.getColor(getAdapterPosition() % 2 != 0 ? R.color.colorAccentVeryLight : R.color.card_color));
            YoYo.with(Techniques.BounceInLeft)
                .duration(400)
                .playOn(itemView);

        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                if (listener != null) {
                    listener.onClick(item);
                }
            } else if (view == btnEdit) {
                if (deleteListener != null) {
                    deleteListener.onClick(item);
                }
            }


        }

    }


}
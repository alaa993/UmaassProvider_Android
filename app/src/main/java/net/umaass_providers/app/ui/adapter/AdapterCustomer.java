package net.umaass_providers.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import net.umaass_providers.app.R;
import net.umaass_providers.app.application.Preference;
import net.umaass_providers.app.data.remote.models.Avatar;
import net.umaass_providers.app.data.remote.models.Customer;
import net.umaass_providers.app.data.remote.utils.DiffCallback;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.utils.CircleImageView;
import net.umaass_providers.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.MyViewHolder> {

    private Context mContext;
    private List<Customer> lists = new ArrayList<>();
    private ItemClickListener<Customer> listener;
    private int lastSelectedPosition;
    private boolean choosingMode;

    public AdapterCustomer() {
    }

    public void setListener(ItemClickListener<Customer> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_customer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Customer item = lists.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public void clearAndPut(List<Customer> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback<>(lists, items, new DiffUtil.ItemCallback<Customer>() {
            @Override
            public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
                return oldItem.equals(newItem);
            }
        }));
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    public void put(List<Customer> items) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback<>(lists, items, new DiffUtil.ItemCallback<Customer>() {
            @Override
            public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
                return oldItem.equals(newItem);
            }
        }));
        if (items != null) {
            lists.addAll(items);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    public void enableChoosingMode() {
        choosingMode = true;
    }

    public Customer getItem(int position) {
        return lists.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Customer item;
        TextView txtName;
        TextView txtPhone;
        CircleImageView imgProfile;

        MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtPhone = view.findViewById(R.id.txtPhone);
            imgProfile = view.findViewById(R.id.imgProfile);
        }

        void bind(Customer i) {
            item = i;
            itemView.setOnClickListener(this);
            txtName.setText(item.getName());
            txtPhone.setMaxLines(2);
            txtPhone.setText(item.getPhone());
            itemView.setBackgroundColor(Utils.getColor(getAdapterPosition() % 2 != 0 ? R.color.colorAccentVeryLight : R.color.card_color));
            Avatar avatar = item.getAvatar();
            if (avatar != null) {
                Preference.setImage(avatar.getUrlMd());
                Picasso.get()
                       .load(avatar.getUrlMd())
                       .placeholder(R.drawable.profile)
                       .error(R.drawable.profile)
                       .into(imgProfile);
            }

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
            }


        }

    }


}
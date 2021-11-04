package net.umaass_providers.app.ui.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.GalleryItem;
import net.umaass_providers.app.interfac.ItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class AdapterGallery extends RecyclerView.Adapter<AdapterGallery.MyViewHolder> {

    private List<GalleryItem> lists = new ArrayList<>();
    private ItemClickListener<GalleryItem> listener;

    public AdapterGallery() {
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_gallery, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final GalleryItem item = lists.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public void clearAndPut(List<GalleryItem> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }

        notifyDataSetChanged();
    }

    public void put(List<GalleryItem> items) {
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }


    public void removeItem(GalleryItem staffItem) {
        int pos = lists.indexOf(staffItem);
        lists.remove(staffItem);
        notifyItemRemoved(pos);
        notifyItemRangeRemoved(pos, lists.size());
    }

    public void setListener(ItemClickListener<GalleryItem> listener) {
        this.listener = listener;
    }

    public GalleryItem getItem(int position) {
        return lists.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        GalleryItem item;
        ImageView img;
        ProgressBar progressBar;

        MyViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.img);
            progressBar = view.findViewById(R.id.progressBar);
        }

        void bind(GalleryItem i) {
            item = i;
            itemView.setOnClickListener(this);

            Picasso.get()
                   .load(i.getUrlMd())
                   .placeholder(R.drawable.holder)
                   .error(R.drawable.holder)
                   .into(new Target() {
                       @Override
                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                           progressBar.setVisibility(View.GONE);
                           img.setImageBitmap(bitmap);
                       }

                       @Override
                       public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                           progressBar.setVisibility(View.GONE);
                           img.setImageDrawable(errorDrawable);
                       }

                       @Override
                       public void onPrepareLoad(Drawable placeHolderDrawable) {
                           progressBar.setVisibility(View.VISIBLE);
                           img.setImageDrawable(placeHolderDrawable);
                       }
                   });
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
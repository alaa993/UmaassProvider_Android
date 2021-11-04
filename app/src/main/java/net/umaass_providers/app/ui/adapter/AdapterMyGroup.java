package net.umaass_providers.app.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import net.umaass_providers.app.R;
import net.umaass_providers.app.interfac.ItemClickListener;
import net.umaass_providers.app.models.ModelIntroducer;
import net.umaass_providers.app.ui.ActivityComments;
import net.umaass_providers.app.utils.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import static net.umaass_providers.app.utils.permission.ActivityUtils.startActivity;

public class AdapterMyGroup extends RecyclerView.Adapter<AdapterMyGroup.MyViewHolder> {


    private List<ModelIntroducer> lists = new ArrayList<>();
    private ItemClickListener<ModelIntroducer> listener;
    Context context;

    public AdapterMyGroup(Context context) {
        this.context = context;
    }

    public void setListener(ItemClickListener<ModelIntroducer> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_interoducer, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        ModelIntroducer item = lists.get(position);
        holder.bind(item);

    }

    public void clearAndPut(List<ModelIntroducer> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }


    public ModelIntroducer getItem(int position) {
        return lists.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ModelIntroducer item;
        TextView txtName;
        TextView txtIncome;
        TextView txtComment,textDelete;
        CircleImageView imgProfile;
        AppCompatRatingBar ratingBar;

        MyViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txtName);
            txtIncome = view.findViewById(R.id.txtIncome);
            imgProfile = view.findViewById(R.id.imgProfile);
            txtComment = view.findViewById(R.id.txtComment);
            textDelete = view.findViewById(R.id.text_delete);
            ratingBar = view.findViewById(R.id.ratingBar);

        }

        void bind(ModelIntroducer i) {
            item = i;
            txtName.setText(item.getName());
            txtIncome.setText("" + item.getIncome());
            ratingBar.setRating(i.getRate());
            if (i.getAvatar() != null) {
                Picasso.get()
                        .load(i.getAvatar().getUrlXs())
                        .placeholder(R.drawable.profile)
                        .error(R.drawable.profile)
                        .into(imgProfile);
            }

            txtComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i.getStaff().size()>0){
                        Intent intent = new Intent(context, ActivityComments.class);
                        intent.putExtra("id", i.getStaff().get(0).getId()+"");
                        startActivity(intent);
                    }

                }
            });

            textDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);

                }
            });
        }
    }
}

package net.umaass_providers.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import net.umaass_providers.app.R;
import net.umaass_providers.app.data.remote.models.Appointment;
import net.umaass_providers.app.interfac.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class AdapterAppointment extends RecyclerView.Adapter<AdapterAppointment.MyViewHolder> {

    private Context mContext;
    private List<Appointment> lists = new ArrayList<>();
    private ItemClickListener<Appointment> listener;
    private int lastSelectedPosition;
    private boolean choosingMode;

    public AdapterAppointment() {
    }

    public void setListener(ItemClickListener<Appointment> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_book_today, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Appointment item = lists.get(position);
        holder.bind(item);

    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public void clearAndPut(List<Appointment> items) {
        lists.clear();
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void put(List<Appointment> items) {
        if (items != null) {
            lists.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void enableChoosingMode() {
        choosingMode = true;
    }

    public Appointment getItem(int position) {
        return lists.get(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Appointment item;
        TextView txtDoctorName;
        TextView txtTypeService;
        TextView txtPatientName;
        TextView txtDate;
        TextView txtTime;

        MyViewHolder(View view) {
            super(view);
            txtDoctorName = view.findViewById(R.id.txtDoctorName);
            txtTypeService = view.findViewById(R.id.txtTypeService);
            txtPatientName = view.findViewById(R.id.txtPatientName);
            txtDate = view.findViewById(R.id.txtDate);
            txtTime = view.findViewById(R.id.txtTime);

        }

        void bind(Appointment i) {
            item = i;
            itemView.setOnClickListener(this);
           // txtDoctorName.setText("Dr. ");
            txtDoctorName.setText(i.getStaff().getName());
            txtTypeService.setText(i.getService().getTitle());
            txtPatientName.setText(i.getClientName());
            String date = i.getStartTime() == null || i.getStartTime().isEmpty() ? i.getFromTo() : i.getStartTime();
            StringTokenizer tk = new StringTokenizer(date);
            if (tk.hasMoreTokens()) {
                txtDate.setText(tk.nextToken());
            }
            if (tk.hasMoreTokens()) {
                txtTime.setText(tk.nextToken());
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
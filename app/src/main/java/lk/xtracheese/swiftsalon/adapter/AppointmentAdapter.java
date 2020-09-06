package lk.xtracheese.swiftsalon.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;

public class AppointmentAdapter extends ListAdapter<Appointment, AppointmentAdapter.MyViewHolder> {

    private static final String TAG = "AppointmentAdapter";
    OnItemClickListener onItemClickListener;
    PicassoImageLoadingService picassoImageLoadingService;

    private static DiffUtil.ItemCallback<Appointment> DIFF_CALLBACK = new DiffUtil.ItemCallback<Appointment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Appointment oldItem, @NonNull Appointment newItem) {
            return false;
        }
    };

    public AppointmentAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_appointments, parent, false);
        return new MyViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ITEM: " + getItem(position).toString());
        picassoImageLoadingService = new PicassoImageLoadingService();


        holder.txtSalonName.setText(getItem(position).getSalonName());
        holder.txtStatus.setText(getItem(position).getStatus());
        holder.txtTime.setText(new StringBuilder(getItem(position).getTime())
                .append(" on " + getItem(position).getDate()));
        picassoImageLoadingService.loadImageRound(getItem(position).getSalonImage() , holder.imgSalon);

    }

    public Appointment getSelectedAppointment(int position) {
        return getItem(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtSalonName, txtStatus, txtTime;
        ImageView imgSalon;

        OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            txtSalonName = itemView.findViewById(R.id.txt_appointment_salon_name);
            txtTime = itemView.findViewById(R.id.txt_appointment_time);
            txtStatus = itemView.findViewById(R.id.txt_appointment_status);
            imgSalon = itemView.findViewById(R.id.img_salon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

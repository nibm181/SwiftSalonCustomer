package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;

public class AppointmentDetailAdapter extends ListAdapter <AppointmentDetail, AppointmentDetailAdapter.MyViewHolder> {

    private static final String TAG = "AppointmentDetailAdapte";

    Context context;

    public AppointmentDetailAdapter(Context context){
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static DiffUtil.ItemCallback<AppointmentDetail> DIFF_CALLBACK = new DiffUtil.ItemCallback<AppointmentDetail>() {
        @Override
        public boolean areItemsTheSame(@NonNull AppointmentDetail oldItem, @NonNull AppointmentDetail newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AppointmentDetail oldItem, @NonNull AppointmentDetail newItem) {
            return oldItem.getAppointmentId() == newItem.getAppointmentId();
        }
    };

    @NonNull
    @Override
    public AppointmentDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_job_price, parent, false);
        return new AppointmentDetailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentDetailAdapter.MyViewHolder holder, int position) {
        holder.txtJobName.setText(getItem(position).getJobName());
        holder.txtJobPrice.setText(String.valueOf(getItem(position).getPrice()));
        holder.txtDiscount.setVisibility(View.GONE);
        holder.txtOffPrice.setVisibility(View.GONE);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtJobName, txtJobPrice, txtOffPrice, txtDiscount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtJobName = itemView.findViewById(R.id.txt_job);
            txtJobPrice = itemView.findViewById(R.id.txt_price);
            txtOffPrice = itemView.findViewById(R.id.txt_discount_price);
            txtDiscount = itemView.findViewById(R.id.txt_discount);
        }
    }
}

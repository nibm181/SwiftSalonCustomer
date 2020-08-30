package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.StylistJob;

public class JobPriceAdapter extends ListAdapter <StylistJob, JobPriceAdapter.MyViewHolder> {

    private static final String TAG = "JobPriceAdapter";

    Context context;

    public JobPriceAdapter(Context context){
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static  DiffUtil.ItemCallback<StylistJob> DIFF_CALLBACK = new DiffUtil.ItemCallback<StylistJob>() {
        @Override
        public boolean areItemsTheSame(@NonNull StylistJob oldItem, @NonNull StylistJob newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StylistJob oldItem, @NonNull StylistJob newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public JobPriceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(context)
                .inflate(R.layout.layout_job_price, parent, false);
        return new JobPriceAdapter.MyViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull JobPriceAdapter.MyViewHolder holder, int position) {

        holder.txtJobName.setText(getItem(position).getName());
        holder.txtJobPrice.setText(String.valueOf(getItem(position).getPrice()));

        if(Common.currentPromotion != null) {
            for(Promotion promotion : Common.currentPromotion){

                if(getItem(position).getJobId() == promotion.getJobId()){
                    holder.txtOffPrice.setText(String.valueOf(promotion.getOffAmount()));
                }
            }
        }else{
            holder.txtOffPrice.setVisibility(View.GONE);
            holder.txtDiscount.setVisibility(View.GONE);
        }


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

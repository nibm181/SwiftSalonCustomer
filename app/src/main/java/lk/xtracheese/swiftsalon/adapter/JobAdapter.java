package lk.xtracheese.swiftsalon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;


import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.StylistJob;

public class JobAdapter extends ListAdapter <StylistJob, JobAdapter.MyViewHolder> {

    private static final String TAG = "JobAdapter";

    JobAdapter jobAdapter;
    Context context;
    List<CardView> cardViewList;
    int oldItem;

    LocalBroadcastManager localBroadcastManager;

    public JobAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private  static DiffUtil.ItemCallback<StylistJob> DIFF_CALLBACK = new DiffUtil.ItemCallback<StylistJob>() {
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_job, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtJobName.setText(getItem(position).getName());
        holder.txtJobDuration.setText(new StringBuilder(getItem(position).getPrice())
                .append(".00 Rs"));
        holder.txtJobPrice.setText(new StringBuilder(String.valueOf(getItem(position).getDuration()))
                .append(" minutes"));

        if(!cardViewList.contains(holder.cardJob))
            cardViewList.add(holder.cardJob);


        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {



                if(holder.getAdapterPosition() != oldItem){
                    cardViewList.get(oldItem).setSelected(false);
                    holder.cardJob.setSelected(true);
                }else {
                    holder.cardJob.setSelected(true);
                }

                oldItem = holder.getAdapterPosition();

                holder.cardJob.setCardElevation(6);


                Common.currentJob = getItem(pos);

                
                //send local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);

                Intent intent1 = new Intent(Common.KEY_JOB_SELECTED);
                localBroadcastManager.sendBroadcast(intent1);
            }
        });
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView txtJobName, txtJobDuration, txtJobPrice;
        CardView cardJob;

        RecyclerItemSelectedListener recyclerItemSelectedListener;


        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtJobName = itemView.findViewById(R.id.txt_job_name);
            txtJobPrice = itemView.findViewById(R.id.txt_job_price);
            txtJobDuration = itemView.findViewById(R.id.txt_job_hour);
            cardJob = itemView.findViewById(R.id.card_job);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}

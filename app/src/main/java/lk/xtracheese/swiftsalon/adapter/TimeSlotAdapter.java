package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.R;

public class TimeSlotAdapter extends ListAdapter<TimeSlot,  TimeSlotAdapter.MyViewholder> {

    private static final String TAG = "TimeSlotAdapter";

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
    int oldItem;

    LocalBroadcastManager localBroadcastManager;

    public TimeSlotAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private static DiffUtil.ItemCallback<TimeSlot> DIFF_CALLBACK = new DiffUtil.ItemCallback<TimeSlot>() {
        @Override
        public boolean areItemsTheSame(@NonNull TimeSlot oldItem, @NonNull TimeSlot newItem) {
            return oldItem.getSlotTiming() == newItem.getSlotTiming();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TimeSlot oldItem, @NonNull TimeSlot newItem) {
            return oldItem.getStatus().equals(newItem.getStatus());
        }
    };

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, parent, false);
        return new MyViewholder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.txtTimeSlot.setText(getItem(position).getSlotTiming());
        holder.getTxtTimeSlotDesc.setText(getItem(position).getStatus());
        holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

        if(holder.getTxtTimeSlotDesc.getText().toString().contains("unavailable")){
            holder.cardTimeSlot.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
            holder.cardTimeSlot.setEnabled(false);
        }

        if(!cardViewList.contains(holder.cardTimeSlot))
            cardViewList.add(holder.cardTimeSlot);

        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {

                if(holder.getAdapterPosition() != oldItem){
                    cardViewList.get(oldItem).setSelected(false);
                    holder.cardTimeSlot.setSelected(true);
                }else {
                    holder.cardTimeSlot.setSelected(true);
                }

                oldItem = holder.getAdapterPosition();

                holder.cardTimeSlot.setCardElevation(6);

                Common.currentTimeSlot = getItem(pos);
                //send local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_STEP, 3);
                localBroadcastManager.sendBroadcast(intent);

                Intent intent1= new Intent(Common.KEY_TIME_SLOT_SELECTED);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }



    public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTimeSlot, getTxtTimeSlotDesc;
        CardView cardTimeSlot;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            txtTimeSlot = itemView.findViewById(R.id.txt_time_slot);
            getTxtTimeSlotDesc = itemView.findViewById(R.id.txt_time_slot_desc);
            cardTimeSlot = itemView.findViewById(R.id.card_time_slot);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}

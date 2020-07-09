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
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.R;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewholder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;

    LocalBroadcastManager localBroadcastManager;
    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, parent, false);
        return new MyViewholder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        holder.txtTimeSlot.setText(timeSlotList.get(position).getSlotTiming());
        holder.getTxtTimeSlotDesc.setText(timeSlotList.get(position).getStatus());
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
                //set background color for selected time slot
                holder.cardTimeSlot.setCardBackgroundColor(
                        context.getResources().getColor(R.color.dark_purple)
                );

                //send local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_TIME_SLOT_SELECTED,timeSlotList.get(pos));
                intent.putExtra(Common.KEY_STEP, 3);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size();
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

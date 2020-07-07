package lk.xtracheese.swiftsalon.Adapter;

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

import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.Interface.OnSalonClickListener;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.Model.Salon;
import lk.xtracheese.swiftsalon.R;

public class SalonAdapter extends ListAdapter<Salon, SalonAdapter.MyViewHolder> {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    OnSalonClickListener onSalonClickListener;

    public SalonAdapter(Context context, OnSalonClickListener onSalonClickListener) {
        super(DIFF_CALLBACK);
        this.onSalonClickListener = onSalonClickListener;
        this.context = context;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private static  DiffUtil.ItemCallback<Salon> DIFF_CALLBACK = new DiffUtil.ItemCallback<Salon>() {
        @Override
        public boolean areItemsTheSame(@NonNull Salon oldItem, @NonNull Salon newItem) {
            return oldItem.getSalID() == newItem.getSalID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Salon oldItem, @NonNull Salon newItem) {
            return oldItem.getSalonName().equals(newItem.getSalonName());
        }
    };

//    public SalonAdapter(Context context, List<Salon> salonList) {
//        this.context = context;
//        this.salonList = salonList;
//        cardViewList = new ArrayList<>();
//        localBroadcastManager = LocalBroadcastManager.getInstance(context);
//    }

    @NonNull
    @Override
    public SalonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, parent, false);
        return new MyViewHolder(itemVIew, onSalonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonAdapter.MyViewHolder holder, int position) {
        holder.txtSalonName.setText(getItem(position).getSalonName());
        holder.txtSalonAddresss.setText(getItem(position).getSalonAddress());
        if(!cardViewList.contains(holder.cardSalon))
            cardViewList.add(holder.cardSalon);

//        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
//            @Override
//            public void onItemSelectedListener(View view, int pos) {
//                //set white background for all card not be selected
//                for(CardView cardView:cardViewList)
//                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
//
//                //set selected background for only selected item
//                holder.cardSalon.setCardBackgroundColor(context.getResources().getColor(R.color.pink));
//                holder.cardSalon.setCardElevation(6);
//
//                //send broadcast to tell booking activity enable button next
//                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                intent.putExtra(Common.KEY_SALON_STORE, salonList.get(pos));
//                intent.putExtra(Common.KEY_STEP, 1);
//                localBroadcastManager.sendBroadcast(intent);
//
//
//            }
//        });
    }

    public Salon getSelectedSalon(int pos){
        Salon salon = getItem(pos);
        return salon;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtSalonName, txtSalonAddresss;
        CardView cardSalon;
        RecyclerItemSelectedListener recyclerItemSelectedListener;
        OnSalonClickListener onSalonClickListener;

//        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
//            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
//        }

        public MyViewHolder(@NonNull View itemView, OnSalonClickListener onSalonClickListener){
            super (itemView);
            this.onSalonClickListener = onSalonClickListener;

            txtSalonAddresss = itemView.findViewById(R.id.txt_salon_addr);
            txtSalonName = itemView.findViewById(R.id.txt_salon_name);
            cardSalon = itemView.findViewById(R.id.card_salon);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSalonClickListener.onSalonClick(getAdapterPosition());
        }
    }
}

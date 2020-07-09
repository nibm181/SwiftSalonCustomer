package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
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

import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.R;

public class SalonAdapter extends ListAdapter<Salon, SalonAdapter.MyViewHolder> {

    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;
    OnItemClickListener onItemClickListener;

    public SalonAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
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


    @NonNull
    @Override
    public SalonAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, parent, false);
        return new MyViewHolder(itemVIew, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonAdapter.MyViewHolder holder, int position) {
        holder.txtSalonName.setText(getItem(position).getSalonName());
        holder.txtSalonAddresss.setText(getItem(position).getSalonAddress1());
        if(!cardViewList.contains(holder.cardSalon))
            cardViewList.add(holder.cardSalon);

    }

    public Salon getSelectedSalon(int pos){
        Salon salon = getItem(pos);
        return salon;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtSalonName, txtSalonAddresss;
        CardView cardSalon;
        RecyclerItemSelectedListener recyclerItemSelectedListener;
        OnItemClickListener onItemClickListener;

//        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
//            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
//        }

        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener){
            super (itemView);
            this.onItemClickListener = onItemClickListener;

            txtSalonAddresss = itemView.findViewById(R.id.txt_salon_addr);
            txtSalonName = itemView.findViewById(R.id.txt_salon_name);
            cardSalon = itemView.findViewById(R.id.card_salon);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
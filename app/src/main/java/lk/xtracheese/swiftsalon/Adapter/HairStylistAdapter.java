package lk.xtracheese.swiftsalon.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.R;

public class HairStylistAdapter extends RecyclerView.Adapter<HairStylistAdapter.MyViewHolder> {

    Context context;
    List<HairStylist> hairStylistList;
    List<CardView> cardViewList;

    LocalBroadcastManager localBroadcastManager;
    public HairStylistAdapter(Context context, List<HairStylist> hairStylistList) {
        this.context = context;
        this.hairStylistList = hairStylistList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_hair_stylist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtHairStylistName.setText(hairStylistList.get(position).getName());
        holder.ratingBar.setRating((float)hairStylistList.get(position).getRating());

        if(!cardViewList.contains(holder.cardHairStylist))
            cardViewList.add(holder.cardHairStylist);

        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background color for all other hair stylist that are not selected
                for(CardView cardView: cardViewList){
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }

                //set background color for selected hair stylist
                holder.cardHairStylist.setCardBackgroundColor(
                        context.getResources().getColor(R.color.pink)
                );
                holder.cardHairStylist.setCardElevation(6);


                //send local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_HAIR_STYLIST_SELECTED,hairStylistList.get(pos));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hairStylistList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtHairStylistName;
        RatingBar ratingBar;
        CardView cardHairStylist;

        RecyclerItemSelectedListener  recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHairStylistName = itemView.findViewById(R.id.txt_hair_stylist_name);
            ratingBar = itemView.findViewById(R.id.rtb_hair_stylist);
            cardHairStylist = itemView.findViewById(R.id.card_hair_stylist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}

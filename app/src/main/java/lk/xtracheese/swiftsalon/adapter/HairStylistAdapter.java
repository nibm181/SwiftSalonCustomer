package lk.xtracheese.swiftsalon.adapter;

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
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.Interface.RecyclerItemSelectedListener;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Stylist;

public class HairStylistAdapter extends ListAdapter<Stylist, HairStylistAdapter.MyViewHolder> {

    Context context;
    List<Stylist> stylistList;
    List<CardView> cardViewList;
    OnItemClickListener onItemClickListener;

    LocalBroadcastManager localBroadcastManager;

    public HairStylistAdapter(Context context, OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
        this.context = context;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private static DiffUtil.ItemCallback<Stylist> DIFF_CALLBACK = new DiffUtil.ItemCallback<Stylist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Stylist oldItem, @NonNull Stylist newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Stylist oldItem, @NonNull Stylist newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_hair_stylist, parent, false);
        return new MyViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtHairStylistName.setText(getItem(position).getName());
        holder.ratingBar.setRating((float) getItem(position).getRating());

        if (!cardViewList.contains(holder.cardHairStylist))
            cardViewList.add(holder.cardHairStylist);


        }

//        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
//            @Override
//            public void onItemSelectedListener(View view, int pos) {
//                //set background color for all other hair stylist that are not selected
//                for (CardView cardView : cardViewList) {
//                    cardView.setCardBackgroundColor(context.getResources()
//                            .getColor(android.R.color.white));
//                }
//
//                //set background color for selected hair stylist
//                holder.cardHairStylist.setCardBackgroundColor(
//                        context.getResources().getColor(R.color.pink)
//                );
//                holder.cardHairStylist.setCardElevation(6);
//
//
//                //send local broadcast to enable button next
//                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                intent.putExtra(Common.KEY_HAIR_STYLIST_SELECTED, getItem(pos));
//                intent.putExtra(Common.KEY_STEP, 2);
//                localBroadcastManager.sendBroadcast(intent);
//            }
//        });

        public Stylist getSelectedSalon(int pos){
            Stylist stylist = getItem(pos);
            return stylist;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtHairStylistName;
        RatingBar ratingBar;
        CardView cardHairStylist;
        OnItemClickListener onItemClickListener;


        public MyViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            txtHairStylistName = itemView.findViewById(R.id.txt_hair_stylist_name);
            ratingBar = itemView.findViewById(R.id.rtb_hair_stylist);
            cardHairStylist = itemView.findViewById(R.id.card_hair_stylist);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}

package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import lk.xtracheese.swiftsalon.activity.BookingActivity;
import lk.xtracheese.swiftsalon.activity.ViewSalonActivity;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;

public class HairStylistAdapter extends ListAdapter<Stylist, HairStylistAdapter.MyViewHolder> {

    private static final String TAG = "HairStylistAdapter";

    Context context;
    List<CardView> cardViewList;
    int oldItem;

    LocalBroadcastManager localBroadcastManager;
    PicassoImageLoadingService picassoImageLoadingService;

    public HairStylistAdapter(Context context ) {
        super(DIFF_CALLBACK);
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
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        picassoImageLoadingService = new PicassoImageLoadingService();

        holder.txtHairStylistName.setText(getItem(position).getName());
        holder.ratingBar.setRating(getItem(position).getRating());
        picassoImageLoadingService.loadImageRound(getItem(position).getImg(), holder.imgStylist);

        if (!cardViewList.contains(holder.cardHairStylist))
            cardViewList.add(holder.cardHairStylist);

        holder.setRecyclerItemSelectedListener(new RecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set background color for all other hair stylist that are not selected
                if(holder.getAdapterPosition() != oldItem){
                    cardViewList.get(oldItem).setSelected(false);
                    holder.cardHairStylist.setSelected(true);
                }else {
                    holder.cardHairStylist.setSelected(true);
                }

                oldItem = holder.getAdapterPosition();

                holder.cardHairStylist.setCardElevation(6);

                Common.currentStylist = getItem(pos);
                //send local broadcast to enable button next
//                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                intent.putExtra(Common.KEY_STEP, 1);
//                localBroadcastManager.sendBroadcast(intent);

                Intent intent = new Intent(view.getContext(), BookingActivity.class);
                view.getContext().startActivity(intent);
                
            }
        });


        }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtHairStylistName;
        RatingBar ratingBar;
        CardView cardHairStylist;
        ImageView imgStylist;

        RecyclerItemSelectedListener recyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(RecyclerItemSelectedListener recyclerItemSelectedListener) {
            this.recyclerItemSelectedListener = recyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView  ) {
            super(itemView);
            imgStylist = itemView.findViewById(R.id.img_hair_stylist);
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

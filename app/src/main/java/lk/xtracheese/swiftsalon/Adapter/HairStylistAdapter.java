package lk.xtracheese.swiftsalon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.R;

public class HairStylistAdapter extends RecyclerView.Adapter<HairStylistAdapter.MyViewHolder> {

    Context context;
    List<HairStylist> hairStylistList;

    public HairStylistAdapter(Context context, List<HairStylist> hairStylistList) {
        this.context = context;
        this.hairStylistList = hairStylistList;
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
    }

    @Override
    public int getItemCount() {
        return hairStylistList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtHairStylistName;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtHairStylistName = itemView.findViewById(R.id.txt_hair_stylist_name);
            ratingBar = itemView.findViewById(R.id.rtb_hair_stylist);
        }
    }
}

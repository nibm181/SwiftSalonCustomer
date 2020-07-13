package lk.xtracheese.swiftsalon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.R;

public class LooKBookAdapter extends ListAdapter<LookBook, LooKBookAdapter.MyViewHolder> {

    Context context;

    public LooKBookAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public static DiffUtil.ItemCallback<LookBook> DIFF_CALLBACK = new DiffUtil.ItemCallback<LookBook>() {
        @Override
        public boolean areItemsTheSame(@NonNull LookBook oldItem, @NonNull LookBook newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull LookBook oldItem, @NonNull LookBook newItem) {
            return oldItem.getImage().equals(newItem.getImage());
        }
    };

    @NonNull
    @Override
    public LooKBookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_look_book, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(getItem(position).getImage())
                .into(holder.imageView);

    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_look_book);
        }
    }
}

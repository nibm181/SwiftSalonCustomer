package lk.xtracheese.swiftsalon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.xtracheese.swiftsalon.Model.LookBook;
import lk.xtracheese.swiftsalon.R;

public class LooKBookAdapter extends RecyclerView.Adapter<LooKBookAdapter.MyViewHolder> {

    Context context;
    List<LookBook> lookBook;

    public LooKBookAdapter(Context context, List<LookBook> lookBook) {
        this.context = context;
        this.lookBook = lookBook;
    }

    @NonNull
    @Override
    public LooKBookAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_look_book, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(lookBook.get(position).getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return lookBook.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_look_book);
        }
    }
}

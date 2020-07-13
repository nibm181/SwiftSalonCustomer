package lk.xtracheese.swiftsalon.service;

import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import lk.xtracheese.swiftsalon.R;
import ss.com.bannerslider.ImageLoadingService;

public class PicassoImageLoadingService implements ImageLoadingService {

    private static final String TAG = "Picasso";


    @Override
    public void loadImage(String url, ImageView imageView) {
        Picasso.get().load(url)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        Picasso.get().load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        errorDrawable = R.drawable.my_bg_proj;
        placeHolder = R.drawable.my_bg_proj;
        Picasso.get().load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
    }
}

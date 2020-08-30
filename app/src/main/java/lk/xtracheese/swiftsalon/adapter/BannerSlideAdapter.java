package lk.xtracheese.swiftsalon.adapter;

import android.util.Log;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Promotion;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSlideAdapter extends SliderAdapter {

    private static final String TAG = "BannerSlideAdapter";
    List<Promotion> promotionList;

    public BannerSlideAdapter(List<Promotion> promotionList) {
        this.promotionList = promotionList;
    }

    @Override
    public int getItemCount() {
        return promotionList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        if(promotionList != null) {
            Log.d(TAG, "onBindImageSlide: "+ promotionList.get(position).getImage());
            imageSlideViewHolder.bindImageSlide(promotionList.get(position).getImage());
        }
    }


}

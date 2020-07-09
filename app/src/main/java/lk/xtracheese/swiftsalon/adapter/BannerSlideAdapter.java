package lk.xtracheese.swiftsalon.adapter;

import android.util.Log;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Banner;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSlideAdapter extends SliderAdapter {

    List<Banner> bannerList;

    public BannerSlideAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
        Log.d("hello", "hello");
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        if(bannerList != null) {
            imageSlideViewHolder.bindImageSlide(bannerList.get(position).getImage());
        }
    }


}

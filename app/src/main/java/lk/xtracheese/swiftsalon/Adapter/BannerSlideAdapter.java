package lk.xtracheese.swiftsalon.Adapter;

import android.util.Log;

import java.util.List;

import lk.xtracheese.swiftsalon.Model.Banner;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class BannerSlideAdapter extends SliderAdapter {

    List<Banner> bannerList;

    public BannerSlideAdapter(List<Banner> bannerList) {
        this.bannerList = bannerList;
        Log.d("from adapter", bannerList.get(1).getImage());
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(bannerList.get(position).getImage());

    }


}

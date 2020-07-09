package lk.xtracheese.swiftsalon.Interface;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Banner;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner>banners);
    void onBannerLoadFailed(String message);
}

package lk.xtracheese.swiftsalon.Interface;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Promotion;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Promotion> promotions);
    void onBannerLoadFailed(String message);
}

package lk.xtracheese.swiftsalon.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lk.xtracheese.swiftsalon.Fragments.BookingStep2Fragment;
import lk.xtracheese.swiftsalon.Fragments.BookingStep3Fragment;
import lk.xtracheese.swiftsalon.Fragments.BookingStep4Fragment;
import lk.xtracheese.swiftsalon.Fragments.bookingStep1Fragment;

public class SearchViewAdapter extends FragmentPagerAdapter {

    public SearchViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return bookingStep1Fragment.getInstance();
            case 1:
                return BookingStep2Fragment.getInstance();
            case 2:
                return BookingStep3Fragment.getInstance();
            case 3:
                return BookingStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

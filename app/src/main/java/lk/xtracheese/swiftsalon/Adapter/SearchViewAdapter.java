package lk.xtracheese.swiftsalon.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lk.xtracheese.swiftsalon.Fragments.SelectJobFragment;
import lk.xtracheese.swiftsalon.Fragments.SelectStylistFragment;
import lk.xtracheese.swiftsalon.Fragments.SelectTimeSlotFragment;
import lk.xtracheese.swiftsalon.Fragments.ConfirmAppointmentFragment;

public class SearchViewAdapter extends FragmentPagerAdapter {

    public SearchViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return SelectStylistFragment.getInstance();
            case 1:
                return SelectJobFragment.newInstance();
            case 2:
                return SelectTimeSlotFragment.getInstance();
            case 3:
                return ConfirmAppointmentFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

package lk.xtracheese.swiftsalon.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import lk.xtracheese.swiftsalon.fragments.AppointmentResultFragment;
import lk.xtracheese.swiftsalon.fragments.SelectJobFragment;
import lk.xtracheese.swiftsalon.fragments.SelectStylistFragment;
import lk.xtracheese.swiftsalon.fragments.SelectTimeSlotFragment;
import lk.xtracheese.swiftsalon.fragments.ConfirmAppointmentFragment;

public class SearchViewAdapter extends FragmentPagerAdapter {

    public SearchViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return SelectJobFragment.getInstance();
            case 1:
                return SelectTimeSlotFragment.getInstance();
            case 2:
                return ConfirmAppointmentFragment.getInstance();
            case 3:
                return AppointmentResultFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}

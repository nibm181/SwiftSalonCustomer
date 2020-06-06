package lk.xtracheese.swiftsalon.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lk.xtracheese.swiftsalon.Adapter.HairStylistAdapter;
import lk.xtracheese.swiftsalon.Common.Common;
import lk.xtracheese.swiftsalon.Common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.R;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    RecyclerView recyclerHairStylist;
    HairStylistAdapter hairStylistAdapter;

    private BroadcastReceiver HairStylistDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<HairStylist> hairStylistArrayList = intent.getParcelableArrayListExtra(Common.KEY_HAIR_STYLIST_LOAD_DONE);

            //initialize adapter
            HairStylistAdapter hairStylistAdapter = new HairStylistAdapter(getContext(), hairStylistArrayList);
            recyclerHairStylist.setAdapter(hairStylistAdapter);
        }
    };
    static BookingStep2Fragment instance;
    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(HairStylistDoneReceiver, new IntentFilter(Common.KEY_HAIR_STYLIST_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(HairStylistDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container, false);
        recyclerHairStylist = itemView.findViewById(R.id.recycler_hair_stylist);

        unbinder = ButterKnife.bind(this, itemView);

        initView();

        return itemView;
    }

    //recycler view customization
    void initView(){
        recyclerHairStylist.setHasFixedSize(true);
        recyclerHairStylist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerHairStylist.addItemDecoration(new SpacesitemDecoration(4));
    }
}

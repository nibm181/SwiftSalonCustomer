package lk.xtracheese.swiftsalon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.HairStylistAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.viewmodel.SelectStylistViewModel;

public class SelectStylistFragment extends Fragment implements OnItemClickListener {


    HairStylistAdapter stylistAdapter;

    RecyclerView recyclerHairStylist;
    SelectStylistViewModel viewModel;


    static SelectStylistFragment instance;

    public static SelectStylistFragment getInstance() {
        if (instance == null)
            instance = new SelectStylistFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container, false);
        recyclerHairStylist = itemView.findViewById(R.id.recycler_hair_stylist);

        initRecyclerView();

        viewModel = new ViewModelProvider(this).get(SelectStylistViewModel.class);
        subscribeObservers();
        getStylistApi();
        return itemView;
    }

    private void getStylistApi() {
        viewModel.stylistApi();
    }

    private void subscribeObservers() {
        viewModel.getStylist().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                if (listResource.data != null) {

                    switch (listResource.status) {
                        case LOADING: {
                            break;
                        }

                        case ERROR: {
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            stylistAdapter.submitList(listResource.data);
                        }

                        case SUCCESS: {
                            stylistAdapter.submitList(listResource.data);
                        }
                    }

                }
            }
        });
    }

    //recycler view customization
    void initRecyclerView() {
        stylistAdapter = new HairStylistAdapter(getActivity(), this);
        recyclerHairStylist.setAdapter(stylistAdapter);
        recyclerHairStylist.setHasFixedSize(true);
        recyclerHairStylist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerHairStylist.addItemDecoration(new SpacesitemDecoration(4));
    }

    @Override
    public void onItemClick(int position) {
        Stylist stylist = stylistAdapter.getSelectedSalon(position);
        Common.currentStylist = stylist;
    }
}

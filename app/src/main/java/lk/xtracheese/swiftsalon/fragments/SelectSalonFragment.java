package lk.xtracheese.swiftsalon.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dmax.dialog.SpotsDialog;
import lk.xtracheese.swiftsalon.adapter.SalonAdapter;
import lk.xtracheese.swiftsalon.activity.BookingActivity;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.Interface.GetDataService;
import lk.xtracheese.swiftsalon.Interface.OnItemClickListener;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.network.RetrofitClientInstance;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.viewmodel.SelectSalonViewModel;
import retrofit2.Call;
import retrofit2.Callback;

public class SelectSalonFragment extends Fragment implements OnItemClickListener {

    private static final String TAG = "SelectSalonFragment";
    static SelectSalonFragment instance;

    SalonAdapter salonAdapter;
    RecyclerView recyclerView;
    EditText txtSearchSalon;

    AlertDialog alertDialog;
    SelectSalonViewModel viewModel;

    String searchText = "";

    public static SelectSalonFragment getInstance(){
        if(instance == null)
            instance = new SelectSalonFragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
         alertDialog = new SpotsDialog.Builder().setContext(getContext()).build();
         alertDialog.show();


        View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container, false);
        recyclerView = itemView.findViewById(R.id.recycler_salon);
        txtSearchSalon = itemView.findViewById(R.id.txt_salon_search);

        initRecyclerView();
        viewModel = new ViewModelProvider(this).get(SelectSalonViewModel.class);
        subscribeObservers();
        getSalonsApi(searchText);

        txtSearchSalon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*Create handle for the RetrofitInstance interface*/
                getSalonsApi(s.toString());
                Log.d(TAG, "onTextChanged: "+s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
//                getSalonsApi(s.toString());
//                Log.d(TAG, "afterTextChanged: "+s.toString());
            }
        });

        alertDialog.dismiss();
        return itemView;

    }

    private void getSalonsApi(String searchText) {
        this.searchText = searchText;
        viewModel.salonsApi(searchText);
    }

    private void subscribeObservers() {
        viewModel.getSalons().observe(getViewLifecycleOwner(), listResource -> {
            if(listResource != null) {
                if(listResource.data != null) {

                    switch (listResource.status) {
                        case LOADING: {
                            Log.d(TAG, "subscribeObservers: LOADING");
                            break;
                        }

                        case ERROR: {
                            Toast.makeText(getContext(), listResource.message, Toast.LENGTH_SHORT).show();
                            salonAdapter.submitList(listResource.data);
                            break;
                        }

                        case SUCCESS: {
//                            Log.d(TAG, "subscribeObservers: DATA: " + listResource.data.toString());
                            Log.d(TAG, "subscribeObservers: call");
                            salonAdapter.submitList(listResource.data);
                            break;
                        }
                    }

                }
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void initRecyclerView() {
        salonAdapter = new SalonAdapter(getActivity(), this);
        recyclerView.setAdapter(salonAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));
    }

    @Override
    public void onItemClick(int position) {
        Salon salon = salonAdapter.getSelectedSalon(position);
        Common.currentSalon = salon;

        Intent intent = new Intent(getContext(), BookingActivity.class);
        startActivity(intent);

    }
}

package lk.xtracheese.swiftsalon.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.adapter.JobAdapter;
import lk.xtracheese.swiftsalon.common.Common;
import lk.xtracheese.swiftsalon.common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.viewmodel.SelectJobViewModel;


public class SelectJobFragment extends Fragment {

    private static final String TAG = "SelectJobFragment";


    JobAdapter jobAdapter;
    RecyclerView recyclerView;
    SelectJobViewModel viewModel;
    DialogService dialogService;

    LocalBroadcastManager localBroadcastManager;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

//                subscribeObservers();
//                getJobApi();

        }
    };

    static SelectJobFragment instance;

    public static SelectJobFragment getInstance() {
        if (instance == null)
            instance = new SelectJobFragment();
        return instance;
    }

    public SelectJobFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ConfirmAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static SelectJobFragment newInstance() {
//        SelectJobFragment fragment = new SelectJobFragment();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(Common.KEY_HAIR_STYLIST_SELECTED));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_select_job, container, false);

        viewModel = new ViewModelProvider(this).get(SelectJobViewModel.class);
        dialogService = new DialogService(getContext());

        recyclerView = itemView.findViewById(R.id.recycler_job);

        subscribeObservers();
        getJobApi();
        return itemView;
    }

    private void getJobApi(){viewModel.StylistJobApi();}

    private void subscribeObservers(){
        viewModel.getStylistJobs().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                if(listResource.data != null){

                    switch (listResource.status){
                        case LOADING:{
                            break;
                        }

                        case ERROR:{
                            dialogService.showToast(listResource.message);
                            initRecyclerView();
                            jobAdapter.submitList(listResource.data);
                        }
                        case SUCCESS:{
                            initRecyclerView();
                            jobAdapter.submitList(listResource.data);
                        }
                    }
                }
            }
        });
    }

    void initRecyclerView(){
        jobAdapter = new JobAdapter(getActivity());
        recyclerView.setAdapter(jobAdapter);
        recyclerView.setHasFixedSize(true);;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));

    }
}
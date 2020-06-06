package lk.xtracheese.swiftsalon.Fragments;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import lk.xtracheese.swiftsalon.Adapter.BannerSlideAdapter;
import lk.xtracheese.swiftsalon.Adapter.SalonAdapter;
import lk.xtracheese.swiftsalon.Common.SpacesitemDecoration;
import lk.xtracheese.swiftsalon.Interface.GetDataService;
import lk.xtracheese.swiftsalon.Model.Banner;
import lk.xtracheese.swiftsalon.Model.Salon;
import lk.xtracheese.swiftsalon.Network.RetrofitClientInstance;
import lk.xtracheese.swiftsalon.R;
import retrofit2.Call;
import retrofit2.Callback;

public class bookingStep1Fragment extends Fragment {

    static  bookingStep1Fragment instance;

    SalonAdapter salonAdapter;
    RecyclerView recyclerView;
    EditText txtSearchSalon;

    AlertDialog alertDialog;



    public static bookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new bookingStep1Fragment();
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

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Salon>> call = service.getSalon();
        call.enqueue(new Callback<List<Salon>>() {
            @Override
            public void onResponse(Call<List<Salon>> call, retrofit2.Response<List<Salon>> response) {
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Salon>> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

         View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container, false);
        recyclerView = itemView.findViewById(R.id.recycler_salon);
        txtSearchSalon = itemView.findViewById(R.id.txt_salon_search);

        initView();

        txtSearchSalon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*Create handle for the RetrofitInstance interface*/
                GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                Call<List<Salon>> call = service.getSearchSalon(s.toString());
                call.enqueue(new Callback<List<Salon>>() {
                    @Override
                    public void onResponse(Call<List<Salon>> call, retrofit2.Response<List<Salon>> response) {
                        generateDataList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Salon>> call, Throwable t) {
                        Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        alertDialog.dismiss();
        return itemView;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }



    //recycler view customization
    void initView(){
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.addItemDecoration(new SpacesitemDecoration(4));
    }

//    List<Salon> getSalon(){
//
//
//        alertDialog.show();
//        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
//        String url = "http://192.168.56.1/SwiftSalon/Salon.php";
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        int length = response.length();
//                        resSalonName = new String[length];
//                        resSalonAddr = new String[length];
//                        try {
//                            if(salonList == null){
//                                salonList = new ArrayList<>();
//                            }else {
//                                Log.d("debug", "else list");
//                                salonList.clear();
//                            }
//
//
//                            for (int i = 0; i < length; i++) {
//
//                                JSONObject salon = response.getJSONObject(i);
//
//                                resSalonName[i] = salon.getString("salonName");
//                                resSalonAddr[i] = salon.getString("salonAddr1");
//
//                            }
//
//                            //convert array to list
//                            for(int i=0; i < length; i++){
//                                salonList.add(new Salon(resSalonName[i], resSalonAddr[i]));
//                            }
//
//                            if(salonAdapter == null){
//                                salonAdapter = new SalonAdapter(getActivity(), salonList);
//                                recyclerView.setAdapter(salonAdapter);
//                            }else {
//                                salonAdapter = new SalonAdapter(getActivity(), salonList);
//                                recyclerView.setAdapter(salonAdapter);
////                                salonAdapter.notifyDataSetChanged();
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//
//        );
//
//        requestQueue.add(jsonArrayRequest);
//        return salonList;
//
//    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Salon> salonList) {
        salonAdapter = new SalonAdapter(getActivity(), salonList);
        recyclerView.setAdapter(salonAdapter);
    }

}

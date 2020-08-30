package lk.xtracheese.swiftsalon.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.FirstActivity;
import lk.xtracheese.swiftsalon.activity.ViewAppointmentsActivity;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;


public class ProfileFragment extends Fragment {

  ImageView imageView;
  TextView txtEditProfile, txtViewAppointments, txtLogout;
  Session session;

    public ProfileFragment() {
        // Required empty public constructor
    }

    static ProfileFragment instance;

    public static ProfileFragment getInstance() {
        if (instance == null)
            instance = new ProfileFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.prof_user_pic);
        txtEditProfile = view.findViewById(R.id.txt_prof_edit_acc);
        txtViewAppointments = view.findViewById(R.id.txt_prof_view_appointments);
        txtLogout = view.findViewById(R.id.txt_prof_logout);

        String userImageURL = "http://10.0.2.2/swiftsalon_api/uploads/user_images/user.jpeg";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);

        txtViewAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAppointmentsActivity.class);
                startActivity(intent);
            }
        });

        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewAppointmentsActivity.class);
                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.clearSession();
                Intent intent = new Intent(getContext(), FirstActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
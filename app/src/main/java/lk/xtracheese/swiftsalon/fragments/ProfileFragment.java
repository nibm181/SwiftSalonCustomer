package lk.xtracheese.swiftsalon.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;


public class ProfileFragment extends Fragment {

  ImageView imageView;
  TextView txtEditProfile, txtViewAppointments, txtLogout;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = view.findViewById(R.id.prof_user_pic);

        String userImageURL = "http://10.0.2.2/SwiftSalon/user_images/user.jpeg";
        PicassoImageLoadingService picassoImageLoadingService = new PicassoImageLoadingService();
        picassoImageLoadingService.loadImage(userImageURL, imageView);
        // Inflate the layout for this fragment
        return view;
    }
}
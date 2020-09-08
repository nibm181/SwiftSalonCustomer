package lk.xtracheese.swiftsalon.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.FirstActivity;
import lk.xtracheese.swiftsalon.activity.UserProfileActivity;
import lk.xtracheese.swiftsalon.activity.ViewAppointmentsActivity;
import lk.xtracheese.swiftsalon.service.DialogService;
import lk.xtracheese.swiftsalon.service.PicassoImageLoadingService;
import lk.xtracheese.swiftsalon.util.Session;


public class ProfileFragment extends Fragment {

    static ProfileFragment instance;
    Session session;
    PicassoImageLoadingService picassoImageLoadingService;
    DialogService dialogService;

    ImageView imageView;
    TextView txtUserName, txtEditProfile, txtViewAppointments, txtLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment getInstance() {
        if (instance == null)
            instance = new ProfileFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
        picassoImageLoadingService = new PicassoImageLoadingService();
        dialogService = new DialogService(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtUserName = view.findViewById(R.id.txt_user_name);
        imageView = view.findViewById(R.id.prof_user_pic);
        txtEditProfile = view.findViewById(R.id.txt_prof_edit_acc);
        txtViewAppointments = view.findViewById(R.id.txt_prof_view_appointments);
        txtLogout = view.findViewById(R.id.txt_prof_logout);

        txtUserName.setText(session.getUsername());
        picassoImageLoadingService.loadImageRound(session.getUserImg(), imageView);

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
                Intent intent = new Intent(getContext(), UserProfileActivity.class);
                startActivity(intent);
            }
        });

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogService.areYouSure().setConfirmClickListener(sweetAlertDialog -> {
                    session.clearSession();
                    Intent intent = new Intent(getContext(), FirstActivity.class);
                    startActivity(intent);
                }).show();

            }
        });

        return view;
    }
}
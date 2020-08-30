package lk.xtracheese.swiftsalon.service;

import android.content.Context;
import android.widget.Button;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;

public class DialogService {

    SweetAlertDialog sweetAlertDialog;
    Context context;

    public DialogService(Context context) {
        this.context = context;
    }

    public SweetAlertDialog  loadingDialog(){
              sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
              sweetAlertDialog.getProgressHelper().setBarColor(R.color.success_stroke_color);
              sweetAlertDialog.setTitleText("Loading");
              sweetAlertDialog.setCancelable(false);
              return sweetAlertDialog;
    }
    public void dismissLoading(){
            sweetAlertDialog.dismissWithAnimation();
    }

    public SweetAlertDialog errorDialog(String message){

        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(message);
        return sweetAlertDialog;
    }

    public SweetAlertDialog oopsErrorDialog(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("Oops! Something went wrong. Try again later");
        return sweetAlertDialog;
    }

    public SweetAlertDialog successAppointmentDialog(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Appointment Requested Successfully");
        return sweetAlertDialog;
    }

    public SweetAlertDialog successAccountDialog(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Account Created Successfully!");
        return sweetAlertDialog;
    }
}

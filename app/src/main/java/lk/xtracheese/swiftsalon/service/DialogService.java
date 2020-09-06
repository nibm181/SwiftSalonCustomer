package lk.xtracheese.swiftsalon.service;

import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lk.xtracheese.swiftsalon.R;

public class DialogService {

    SweetAlertDialog sweetAlertDialog;
    Toast toast;
    Context context;
    int duration = Toast.LENGTH_SHORT;

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

    public SweetAlertDialog appointmentCancelled(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Appointment Cancelled successfully!");
        return sweetAlertDialog;
    }

    public SweetAlertDialog notConnected(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Your are not connected, " +
                "data you see may differ from the real time data! " +
                "Please connect with internet to see real time data.");
        return sweetAlertDialog;
    }

    public SweetAlertDialog ratingSMessage(){
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        sweetAlertDialog.setTitleText("Thank you!");
        return sweetAlertDialog;
    }

    public void showToast(String msg){
        toast = Toast.makeText(context, msg, duration );
        toast.show();
    }
}

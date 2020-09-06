package lk.xtracheese.swiftsalon.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RatingBar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import lk.xtracheese.swiftsalon.R;
import lk.xtracheese.swiftsalon.activity.BookingActivity;
import lk.xtracheese.swiftsalon.activity.HomeActivity;
import lk.xtracheese.swiftsalon.activity.MainActivity;
import lk.xtracheese.swiftsalon.activity.RatingActivity;
import lk.xtracheese.swiftsalon.activity.ViewAppointmentDetailActivity;
import lk.xtracheese.swiftsalon.activity.ViewAppointmentsActivity;
import lk.xtracheese.swiftsalon.model.Appointment;

import static lk.xtracheese.swiftsalon.util.Constants.STATUS_CANCELED;
import static lk.xtracheese.swiftsalon.util.Constants.STATUS_COMPLETED;
import static lk.xtracheese.swiftsalon.util.Constants.STATUS_ONSCHEDULE;

public class NotificationHelper {

    private static final String TAG = "NotificationHelper";

    public static void showNotification(Context context, Appointment appointment) {

        String title = "";
        String body = "";
        Intent resultIntent = null;
        Log.d(TAG, "showNotification: Notification: " + appointment.toString());
        if(appointment.getStatus().equals(STATUS_CANCELED)) {
            title = "Appointment Canceled";
            body = "An appointment from " + appointment.getSalonName() + " has been canceled.";
            // Create an Intent for the activity you want to start
            resultIntent = new Intent(context, ViewAppointmentDetailActivity.class);
            resultIntent.putExtra("appointment", appointment);
        }
        else if(appointment.getStatus().equals(STATUS_COMPLETED)) {
            title = "Appointment completed";
            body = "Your appointment with " + appointment.getSalonName() + " has successfully completed";
            // Create an Intent for the activity you want to start
            resultIntent = new Intent(context, RatingActivity.class);
            resultIntent.putExtra("appointment", appointment);
        }
        else if(appointment.getStatus().equals(STATUS_ONSCHEDULE)) {
            title = "Appointment Accepted";
            body = "Your request for an appointment with " + appointment.getSalonName() + " has been accepted!";
            // Create an Intent for the activity you want to start
            resultIntent = new Intent(context, ViewAppointmentDetailActivity.class);
            resultIntent.putExtra("appointment", appointment);
        }


        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);

        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, HomeActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        builder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
        builder.setAutoCancel(true);
        manager.notify(1, builder.build());
    }

    public static void showNotification(Context context, String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, HomeActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(1, builder.build());
    }
}

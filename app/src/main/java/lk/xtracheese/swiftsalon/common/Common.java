package lk.xtracheese.swiftsalon.common;

import java.util.Calendar;
import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.model.TimeSlot;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE" ;
    public static final String KEY_HAIR_STYLIST_LOAD_DONE = "HAIR_STYLIST_LOAD_DONE";
    public static final String KEY_TIME_SLOT_LOAD_DONE = "KEY_TIME_SLOT_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_HAIR_STYLIST_SELECTED = "HAIR_STYLIST_SELECTED";
    public static final String KEY_TIME_SLOT_SELECTED = "KEY_TIME_SLOT_SELECTED";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String KEY_JOB_SELECTED = "JOB_SELECTED";
    public static final String KEY_APPOINTMENT_SENT = "KEY_APPOINTMENT_SENT";
    public static int step = 0;
    public static List<Promotion> currentPromotion;
    public static Salon currentSalon;
    public static Appointment currentAppointment;
    public static List<StylistJob> currentJob;
    public static Stylist currentStylist;
    public static TimeSlot currentTimeSlot;
    public static Calendar currentDate = Calendar.getInstance();
}

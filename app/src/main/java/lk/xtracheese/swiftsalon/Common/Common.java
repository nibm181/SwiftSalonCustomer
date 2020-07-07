package lk.xtracheese.swiftsalon.Common;

import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

import lk.xtracheese.swiftsalon.Model.HairStylist;
import lk.xtracheese.swiftsalon.Model.Salon;
import lk.xtracheese.swiftsalon.Model.TimeSlot;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE" ;
    public static final String KEY_HAIR_STYLIST_LOAD_DONE = "HAIR_STYLIST_LOAD_DONE";
    public static final String KEY_TIME_SLOT_LOAD_DONE = "KEY_TIME_SLOT_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_HAIR_STYLIST_SELECTED = "HAIR_STYLIST_SELECTED";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final String KEY_TIME_SLOT_SELECTED = "KEY_TIME_SLOT_SELECTED";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static Salon currentSalon;
    public static int step = 0;
    public static HairStylist currentHairStylist;
    public static TimeSlot currentTimeSlot;
    public static Calendar currentDate = Calendar.getInstance();
}

package lk.xtracheese.swiftsalon.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.model.Banner;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.Job;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.model.TimeSlot;
import lk.xtracheese.swiftsalon.model.User;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SwiftSalonDao {


    @Insert(onConflict = REPLACE)
    void insertAppointment(Appointment appointment);

    @Insert(onConflict = REPLACE)
    void insertAppointmentDetails(AppointmentDetail... appointmentDetails);

    @Update
    void updateAppointment(Appointment appointment);

    @Query("UPDATE tbl_appointment SET status = :status, modified_on = :modifiedOn WHERE id = :id")
    void updateAppointmentStatus(int id, String status, int modifiedOn);

    @Query("SELECT * FROM tbl_appointment WHERE id = :id")
    LiveData<Appointment> getAppointment(int id);

    @Query("SELECT * FROM tbl_appointment WHERE salon_id = :salonId")
    LiveData<List<Appointment>> getAppointments(int salonId);

    @Query("SELECT * FROM tbl_appointment WHERE salon_id = :salonId AND status = 'pending'")
    LiveData<List<Appointment>> getNewAppointments(int salonId);

    @Query("SELECT * FROM tbl_appointment WHERE salon_id = :salonId AND status = 'on schedule'")
    LiveData<List<Appointment>> getOngoingAppointments(int salonId);


    //LookBook

    @Insert(onConflict = REPLACE)
    void insertLookBooks(LookBook... lookBooks);

    @Query("SELECT * from tbl_look_book")
    LiveData<List<LookBook>> getLookBooks();

    //Promotion

    @Insert(onConflict = REPLACE)
    void insertBanners(Banner... banners);

    @Query("SELECT * from tbl_promotion")
    LiveData<List<Banner>> getBanners();

    //Stylistjobs
    @Insert(onConflict = REPLACE)
    void insertStylistJobs(StylistJob... stylistJobs);

    @Query("SELECT * from tbl_stylist_job WHERE stylist_id = :stylistId")
    LiveData<List<StylistJob>> getStylistJobs(int stylistId);


    // Salon
    @Insert(onConflict = REPLACE)
    void insertSalon(Salon salon);

    @Insert(onConflict = REPLACE)
    void insertSalons(Salon... salons);

//    @Query("SELECT * FROM tbl_salon")
//    LiveData<List<Salon>> getSalons();

    @Query("SELECT tbl_salon.* FROM tbl_salon LEFT JOIN tbl_job on tbl_salon.id = tbl_job.salon_id WHERE tbl_salon.name LIKE :searchTxt OR tbl_job.name LIKE :searchTxt")
    LiveData<List<Salon>> getSalons(String searchTxt);



    // Stylist
    @Insert(onConflict = REPLACE)
    void insertStylists(Stylist... stylists);

    @Query("SELECT * FROM tbl_stylist WHERE salon_id = :salonId")
    LiveData<List<Stylist>> getStylists(int salonId);

    //User
    @Insert(onConflict = REPLACE)
    void insertUser(User user);

}

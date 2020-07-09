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
import lk.xtracheese.swiftsalon.model.TimeSlot;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SwiftSalonDao {

    // Appointment
    @Insert(onConflict = IGNORE)
    long[] insertAppointments(Appointment... appointments);

    @Insert(onConflict = REPLACE)
    void insertAppointment(Appointment appointment);

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

    // Appointment Detail
    @Insert(onConflict = REPLACE)
    void insertAppointmentDetails(AppointmentDetail... details);

    @Query("SELECT * FROM tbl_appointment_detail WHERE appointment_id = :appointmentId")
    LiveData<List<AppointmentDetail>> getAppointmentDetails(int appointmentId);

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

    //TimeSlots

    @Insert(onConflict = REPLACE)
    void insertTimeSlots(TimeSlot... timeSlots);

    @Query("SELECT * from tbl_promotion")
    LiveData<List<Banner>> getTimeSlots();

    // Salon
    @Insert(onConflict = REPLACE)
    void insertSalon(Salon salon);

    @Insert(onConflict = REPLACE)
    void insertSalons(Salon... salons);

    @Query("SELECT * FROM tbl_salon")
    LiveData<List<Salon>> getSalons();

    @Query("SELECT * FROM tbl_salon WHERE id = :id")
    LiveData<Salon> getSalon(int id);

    @Query("SELECT * FROM tbl_salon WHERE id = :id")
    Salon getRawSalon(int id);

    // Job
    @Insert(onConflict = IGNORE)
    long[] insertJobs(Job... jobs);

    @Insert(onConflict = REPLACE)
    void insertJob(Job job);

    @Query("SELECT * FROM tbl_job tj INNER JOIN tbl_stylist_job tsj ON tj.id = tsj.jobId WHERE tsj.stylistId = :stylistId")
    LiveData<List<Job>> getJobs(int stylistId);

    @Query("SELECT * FROM tbl_job WHERE id = :id")
    LiveData<Job> getJob(int id);

    @Query("UPDATE tbl_job SET name = :name, duration = :duration, price = :price WHERE id = :id")
    void updateJob(int id, String name, int duration, float price);

    @Delete
    void deleteJob(Job job);

    @Query("DELETE FROM tbl_job")
    void deleteJobs();

    // Stylist
    @Insert(onConflict = REPLACE)
    void insertStylists(Stylist... stylists);

    @Insert(onConflict = REPLACE)
    void insertStylist(Stylist stylist);

    @Query("UPDATE tbl_stylist SET name = :name, gender = :gender, image = :image, status = :status WHERE id = :id")
    void updateStylist(int id, String name, String gender, String image, boolean status);

    @Query("SELECT * FROM tbl_stylist WHERE salon_id = :salonId")
    LiveData<List<Stylist>> getStylists(int salonId);
}

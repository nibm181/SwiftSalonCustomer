package lk.xtracheese.swiftsalon.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import lk.xtracheese.swiftsalon.model.Appointment;
import lk.xtracheese.swiftsalon.model.AppointmentDetail;
import lk.xtracheese.swiftsalon.model.Promotion;
import lk.xtracheese.swiftsalon.model.LookBook;
import lk.xtracheese.swiftsalon.model.Salon;
import lk.xtracheese.swiftsalon.model.Stylist;
import lk.xtracheese.swiftsalon.model.StylistJob;
import lk.xtracheese.swiftsalon.model.User;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SwiftSalonDao {


    @Insert(onConflict = REPLACE)
    void insertAppointment(Appointment appointment);

    @Insert(onConflict = REPLACE)
    void insertAppointments(Appointment... appointments);

    @Insert(onConflict = REPLACE)
    void insertAppointmentDetail(AppointmentDetail... appointmentDetails);

    @Query("SELECT * FROM tbl_appointment_detail WHERE appointment_id = :id")
    LiveData<List<AppointmentDetail>> getAppointmentDetail(int id);

    @Update
    void updateAppointment(Appointment appointment);

    @Query("UPDATE tbl_appointment SET status = :status, modified_on = :modifiedOn WHERE id = :id")
    void updateAppointmentStatus(int id, String status, int modifiedOn);

    @Query("SELECT * FROM tbl_appointment WHERE id = :id")
    LiveData<Appointment> getAppointment(int id);

    @Query("SELECT * FROM tbl_appointment ORDER BY date DESC")
    LiveData<List<Appointment>> getAppointments();

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
    void insertPromotions(Promotion... promotions);

    @Query("SELECT * from tbl_promotion WHERE end_date >= :today")
    LiveData<List<Promotion>> getPromotions(long today);

    @Query("DELETE FROM tbl_promotion")
    void deletePromotions();

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

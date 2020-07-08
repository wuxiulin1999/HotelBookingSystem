package booksys.application.persistency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Month;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.Customer;
import booksys.application.domain.Reservation;
import booksys.application.domain.Room;
import booksys.storage.Database;

public class BookingMapper {
  // Singleton:

  private static BookingMapper uniqueInstance;

  public static BookingMapper getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new BookingMapper();
    }
    return uniqueInstance;
  }

  public List<Booking> getBookings(Month Month) {
    List<Booking> v = new ArrayList<Booking>();
    try {
      Database.getInstance();
      Statement stmt = Database.getConnection().createStatement();
      ResultSet rset = stmt.executeQuery("SELECT * FROM Reservation WHERE month='" + Month + "'");
      while (rset.next()) {
        int oid = rset.getInt("oid");
        int covers = rset.getInt("covers");
        LocalDate bbmonth = LocalDate.parse(rset.getString("month"));
        Month bmonth = bbmonth.getMonth();
        LocalDate bdate = LocalDate.parse(rset.getString("date"));
        int room = rset.getInt("room_id");
        int cust = rset.getInt("customer_id");
        String aDates = rset.getString("arrivalDate");
        LocalDate adate = null;
        if (aDates != null) {
          adate = LocalDate.parse(aDates);
        }
        PersistentRoom ro = RoomMapper.getInstance().getRoomForOid(room);
        PersistentCustomer c = CustomerMapper.getInstance().getCustomerForOid(cust);
		PersistentReservation r = new PersistentReservation(oid, covers, bmonth, bdate, ro, c, adate);
        v.add(r);
      }
      rset.close();
      rset = stmt.executeQuery("SELECT * FROM WalkIn WHERE month='" + Month + "'");
      while (rset.next()) {
        int oid = rset.getInt("oid");
        int covers = rset.getInt("covers");
        LocalDate bbmonth = LocalDate.parse(rset.getString("month"));
        Month bmonth=bbmonth.getMonth();
        LocalDate bdate = LocalDate.parse(rset.getString("date"));
        int room = rset.getInt("room_id");
        PersistentRoom r = RoomMapper.getInstance().getRoomForOid(room);
        PersistentWalkIn w = new PersistentWalkIn(oid, covers, bmonth, bdate, r);
        v.add(w);
      }
      rset.close();
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return v;
  }

  public PersistentReservation addReservation(int covers,  Month month,LocalDate date, Room room, Customer customer, LocalDate arrivalDate) {
    int oid = Database.getInstance().getId();
    performUpdate("INSERT INTO Reservation " + "VALUES ('" + oid + "', '" + covers + "', '" + month.toString() + "', '" + date.toString() + "', '"
        + ((PersistentRoom) room).getId() + "', '" + ((PersistentCustomer) customer).getId() + "', "
        + (arrivalDate == null ? "NULL" : ("'" + arrivalDate.toString() + "'")) + ")");
    return new PersistentReservation(oid, covers, month, date, room, customer, arrivalDate);
  }

  public PersistentWalkIn addWalkIn(int covers, Month month, LocalDate date, Room room) {
    int oid = Database.getInstance().getId();
    performUpdate(
        "INSERT INTO WalkIn " + "VALUES ('" + oid + "', '" + covers + "', '" + month + "', '" + date + "', '" + ((PersistentRoom) room).getId() + "')");
    return new PersistentWalkIn(oid, covers, month, date, room);
  }

  public void updateBooking(Booking b) {
    PersistentBooking pb = (PersistentBooking) b;
    boolean isReservation = b instanceof Reservation;
    StringBuffer sql = new StringBuffer(128);

    sql.append("UPDATE ");
    sql.append(isReservation ? "Reservation" : "WalkIn");
    sql.append(" SET ");
    sql.append(" covers = ");
    sql.append(pb.getCovers());
    sql.append(", month = '");
    sql.append(pb.getMonth().toString());
    sql.append("', date = '");
    sql.append(pb.getDate().toString());
    sql.append("', room_id = ");
    sql.append(((PersistentRoom) pb.getRoom()).getId());
    if (isReservation) {
      PersistentReservation pr = (PersistentReservation) pb;
      sql.append(", customer_id = ");
      sql.append(((PersistentCustomer) pr.getCustomer()).getId());
      sql.append(", arrivalDate = ");
      LocalDate adate = pr.getArrivalDate();
      if (adate == null) {
        sql.append("NULL");
      } else {
        sql.append("'" + adate + "'");
      }
    }
    sql.append(" WHERE oid = ");
    sql.append(pb.getId());

    performUpdate(sql.toString());
  }

  public void deleteBooking(Booking r) {
    String room = r instanceof Reservation ? "Reservation" : "WalkIn";
    performUpdate("DELETE FROM " +room + " WHERE rowid = '" + ((PersistentBooking) r).getId() + "'");
  }

  private void performUpdate(String sql) {
    try {
      Database.getInstance();
      Statement stmt = Database.getConnection().createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

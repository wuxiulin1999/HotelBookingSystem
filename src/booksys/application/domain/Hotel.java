package booksys.application.domain;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import booksys.application.persistency.BookingMapper;
import booksys.application.persistency.CustomerMapper;
import booksys.application.persistency.RoomMapper;

class Hotel {
  BookingMapper  bm = BookingMapper.getInstance();
  CustomerMapper cm = CustomerMapper.getInstance();
  RoomMapper    rm = RoomMapper.getInstance();

  List<Booking> getBookings(Month currentMonth) {
    return bm.getBookings(currentMonth);
  }

  Customer getCustomer(String name, String phone) {
    return cm.getCustomer(name, phone);
  }

  Room getRoom(int n) {
    return rm.getRoom(n);
  }

  static List<Room> getRooms() {
    return RoomMapper.getInstance().getRooms();
  }

  public Booking addReservation(int covers, Month month, LocalDate date, int rno, String name, String phone) {
	  Room r = getRoom(rno);
	  Customer c = getCustomer(name, phone);
	  return bm.addReservation(covers, month, date, r, c, null);
 }

  public Booking addWalkIn(int covers, Month month, LocalDate date, int rno) {
	  Room r = getRoom(rno);
	  return bm.addWalkIn(covers, month, date, r);
  }

  public void updateBooking(Booking b) {
	  bm.updateBooking(b);
  }

  public void removeBooking(Booking b) {
	  bm.deleteBooking(b);
  }
}
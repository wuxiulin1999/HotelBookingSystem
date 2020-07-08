package booksys.application.domain;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
  // Attributes:

  private Month  Month;
  // Associations:
  private Hotel            hotel = null;
  private List<Booking>         currentBookings;
  private Booking               selectedBooking;

  private List<BookingObserver> observers  = new ArrayList<BookingObserver>();
  private static BookingSystem  uniqueInstance;

  private BookingSystem() {
    hotel = new Hotel();
  }

  public static BookingSystem getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new BookingSystem();
    }
    return uniqueInstance;
  }

  public void addObserver(BookingObserver o) {
    observers.add(o);
  }

  public void notifyObservers() {
    for (BookingObserver bo : observers) {
      bo.update();
    }
  }

  public boolean observerMessage(String message, boolean confirm) {
    BookingObserver bo = (BookingObserver) observers.get(0);
    return bo.message(message, confirm);
  }

  public void setMonth(Month month) {
    Month = month;
    currentBookings = hotel.getBookings(Month);
    selectedBooking = null;
    notifyObservers();
  }

  public boolean addReservation(int covers, Month month, LocalDate date, int rno, String name, String phone) {
    if (!checkDoubleBooked(date, rno, null) && !checkOverbooking(rno, covers)) {
      Booking b = hotel.addReservation(covers, month, date, rno, name, phone);
      currentBookings.add(b);
      notifyObservers();
      return true;
    }
    return false;
  }

  public boolean addWalkIn(int covers, Month month, LocalDate date, int rno) {
    if (!checkDoubleBooked(date, rno, null) && !checkOverbooking(rno, covers)) {
      Booking b = hotel.addWalkIn(covers, month, date, rno);
      currentBookings.add(b);
      notifyObservers();
      return true;
    }
    return false;
  }

  public void selectBooking(int rno, LocalDate date) {
    selectedBooking = null;
    for (Booking b : currentBookings) {
      if (b.getRoomNumber() == rno) {
        if (b.getDate().isBefore(date) && b.getEndDate().isAfter(date)) {
          selectedBooking = b;
        }
      }
    }
    notifyObservers();
  }

  public void cancelSelected() {
    if (selectedBooking != null) {
      if (observerMessage("Are you sure?", true)) {
        currentBookings.remove(selectedBooking);
        hotel.removeBooking(selectedBooking);
        selectedBooking = null;
        notifyObservers();
      }
    }
  }

  public void recordArrival(LocalDate date) {
    if (selectedBooking != null) {
      if (selectedBooking.getArrivalDate() != null) {
        observerMessage("Arrival already recorded", false);
      } else {
        selectedBooking.setArrivalDate(date);
        hotel.updateBooking(selectedBooking);
        notifyObservers();
      }
    }
  }

  public void changeSelected(LocalDate date, int rno) {
    System.out.println("transfering");
    if (selectedBooking != null) {
      if (!checkDoubleBooked(date, rno, selectedBooking) && !checkOverbooking(rno, selectedBooking.getCovers())) {
        selectedBooking.setDate(date);
        selectedBooking.setRoom(hotel.getRoom(rno));
        hotel.updateBooking(selectedBooking);
      }
      notifyObservers();
    }
  }

  private boolean checkDoubleBooked(LocalDate startDate, int rno, Booking ignore) {
    boolean doubleBooked = false;

    LocalDate endDate = startDate.plusDays(1);
    for (Booking b : currentBookings) {
      if (b != ignore && b.getRoomNumber() == rno && startDate.isBefore(b.getEndDate()) && endDate.isAfter(b.getDate())) {
        doubleBooked = true;
        observerMessage("Double booking!", false);
      }
    }
    return doubleBooked;
  }

  private boolean checkOverbooking(int rno, int covers) {
    boolean overflow = false;
    Room r = hotel.getRoom(rno);

    if (r.getSize() < covers) {
      overflow = !observerMessage("Ok to overfill room?", true);
    }
    return overflow;
  }

  public Month getMonth() {
    return Month;
  }

  public List<Booking> getBookings() {
    return new ArrayList<Booking>(currentBookings);
  }

  public Booking getSelectedBooking() {
    return selectedBooking;
  }

  public static List<Room> getRooms() {
    return Hotel.getRooms();
  }
}

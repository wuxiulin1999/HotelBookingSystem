
package booksys.application.persistency;

import java.time.Month;

import booksys.application.domain.Customer;
import booksys.application.domain.Reservation;
import booksys.application.domain.Room;

import java.time.LocalDate;
import java.time.LocalTime;


//(int c, Month m, LocalDate d, Room ro, Customer cust, LocalDate arr)

class PersistentReservation extends Reservation implements PersistentBooking {
	  private int oid;

	  public PersistentReservation(int id, int c, Month m, LocalDate d, Room ro, Customer cust, LocalDate arr) {
	    super(c, m, d, ro, cust, arr);
	    oid = id;
	  }

	  public int getId() {
	    return oid;
	  }
	  
	  public PersistentRoom getRoom() {
		return this.getRoom();	  
	  }
	}

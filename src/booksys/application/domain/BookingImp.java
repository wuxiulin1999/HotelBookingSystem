package booksys.application.domain;

import java.time.LocalDate;
import java.time.Month;

public abstract class BookingImp implements Booking {
	protected int       covers;
	protected Month month;
	protected LocalDate date;
	protected Room     room;

	public BookingImp(int c, Month m, LocalDate d, Room ro) {
		covers = c;
		month = m;
		date = d;
		room = ro;
	}

	public LocalDate getArrivalDate() {
		return null;
	}

	public int getCovers() {
		return covers;
	}

	public Month getMonth() {
		return month;
	}

	public LocalDate getEndDate() {
		return date.plusDays(1);// End time defaults to 1 day after time of booking
	}

	public LocalDate getDate() {
		return date;
	}

 	public Room getRoom() {
 		return room;
 	}
 	
 	public int getRoomNumber() {
 		return room.getNumber();
 	}

 	public void setArrivalDate(LocalDate d) {
 	}

 	public void setCovers(int c) {
 		covers = c;
 	}

 	public void setMonth(Month m) {
 		month = m;
 	}

 	public void setDate(LocalDate d) {
 		date = d;
 	}

 	public void setRoom(Room r) {
 		room = r;
 	}
}

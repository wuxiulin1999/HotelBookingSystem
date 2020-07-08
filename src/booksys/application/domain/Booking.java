package booksys.application.domain;
import java.time.LocalDate;
import java.time.Month;

public interface Booking {
	public LocalDate getArrivalDate();

	public int getCovers();

	public Month getMonth();

	public LocalDate getEndDate();

	public LocalDate getDate();

	public Room getRoom();

	public int getRoomNumber();

	public String getDetails();

	public void setArrivalDate(LocalDate d);

	public void setCovers(int c);

	public void setMonth(Month m);

	public void setDate(LocalDate d);

	public void setRoom(Room r);
}

package booksys.application.domain;

import java.time.LocalDate;
import java.time.Month;

public class WalkIn extends BookingImp {
  public WalkIn(int c, Month month, LocalDate date, Room ro) {
    super(c, month, date, ro);
  }

  public String getDetails() {
    return "Walk-in (" + covers + ")";
  }
}

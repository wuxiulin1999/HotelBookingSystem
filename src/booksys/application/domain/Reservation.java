package booksys.application.domain;

import java.time.LocalDate;
import java.time.Month;

public class Reservation extends BookingImp {
  private Customer  customer;
  private LocalDate arrivalDate;

  public Reservation(int c, Month m, LocalDate d, Room ro, Customer cust, LocalDate arr) {
    super(c, m, d, ro);
    customer = cust;
    arrivalDate = arr;
  }

  public LocalDate getarrivalDate() {
    return arrivalDate;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getDetails() {
    StringBuffer details = new StringBuffer(64);
    details.append(customer.getName());
    details.append(" ");
    details.append(customer.getPhoneNumber());
    details.append(" (");
    details.append(covers);
    details.append(")");
    if (arrivalDate != null) {
      details.append(" [");
      details.append(arrivalDate);
      details.append("]");
    }
    return details.toString();
  }

  public void setArrivalDate(LocalDate d) {
    arrivalDate = d;
  }
}

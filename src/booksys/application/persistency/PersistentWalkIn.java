
package booksys.application.persistency;

import java.time.Month;
import java.time.LocalDate;

import booksys.application.domain.*;

class PersistentWalkIn extends WalkIn implements PersistentBooking {
  private int oid;

  public PersistentWalkIn(int id, int c, Month month, LocalDate date, Room ro) {
    super(c,  month, date, ro);
    oid = id;
  }

  /* public because getId defined in an interface and hence public */

  public int getId() {
    return oid;
  }

@Override
public Month getMonth() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public PersistentRoom getRoom() {
	// TODO Auto-generated method stub
	return null;
}
}

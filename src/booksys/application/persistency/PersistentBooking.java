
package booksys.application.persistency ;

import java.time.Month;

import booksys.application.domain.* ;

interface PersistentBooking extends Booking
{
  int getId() ;

Month getMonth();

PersistentRoom getRoom();
}

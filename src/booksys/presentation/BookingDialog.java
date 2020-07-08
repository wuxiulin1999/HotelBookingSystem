
package booksys.presentation;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.BookingSystem;
import booksys.application.domain.Room;

abstract class BookingDialog extends Dialog {
  private static final long serialVersionUID = -3474526819964168549L;
  protected Choice          roomNumber;
  protected TextField       covers;
  protected TextField       date;
  protected Label           roomNumberLabel;
  protected Label           coversLabel;
  protected Label           dateLabel;
  
  
  
  protected boolean         confirmed;
  protected Button          ok;
  protected Button          cancel;

  BookingDialog(Frame owner, String title) {
    this(owner, title, null);
  }

  // This constructor initializes fields with data from an existing booking.
  // This is useful for completing Exercise 7.6.

  BookingDialog(Frame owner, String title, Booking booking) {
    super(owner, title, true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        confirmed = false;
        BookingDialog.this.hide();
      }
    });

    roomNumberLabel = new Label("Room Number:", Label.RIGHT);
    roomNumber = new Choice();
    List<Room> enumV = BookingSystem.getRooms();
    for(Room t: enumV) {
      roomNumber.add(Integer.toString(t.getNumber()) + " (" + t.getSize()+")");
    }
    if (booking != null) {
      roomNumber.select(Integer.toString(booking.getRoom().getNumber()) + " (" + booking.getRoom().getSize()+")");
    }

    coversLabel = new Label("Covers:", Label.RIGHT);
    covers = new TextField(4);
    if (booking != null) {
      covers.setText(Integer.toString(booking.getCovers()));
    }

    dateLabel = new Label("Date:", Label.RIGHT);
    date = new TextField("YYYY-MM-DD", 8);
    if (booking != null) {
      date.setText(booking.getDate().toString());
    }

    ok = new Button("OK");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = true;
        BookingDialog.this.hide();
      }
    });

    cancel = new Button("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = false;
        BookingDialog.this.hide();
      }
    });
  }

  int getRoomNumber() {
    return Integer.parseInt(roomNumber.getSelectedItem().split(" ")[0]);
  }
  
  int getCovers() {
    return Integer.parseInt(covers.getText());
  }

  LocalDate getDate() {
    return LocalDate.parse(date.getText());
  }

  boolean isConfirmed() {
    return confirmed;
  }
}

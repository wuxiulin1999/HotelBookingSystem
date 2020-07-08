
package booksys.presentation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import booksys.application.domain.Booking;
import booksys.application.domain.BookingObserver;
import booksys.application.domain.BookingSystem;
import booksys.application.domain.Room;

public class StaffUI extends Canvas implements BookingObserver {
  private static final long serialVersionUID = 4540587316285438139L;
  final static int          LEFT_MARGIN      = 50;
  final static int          TOP_MARGIN       = 50;
  final static int          BOTTOM_MARGIN    = 50;
  final static int          ROW_HEIGHT       = 30;
  final static int          COL_WIDTH        = 40;

  final static int          PPM              = 2;                   // Pixels per minute
  final static int          PPH              = 60 * PPM;            // Pixels per hours
  final static int          TZERO            = 1;                  // Earliest time shown
  final static int          SLOTS            = 31;                  // Number of booking slots shown

  // Routines to convert between (x, y) and (time, table)

  private int dateToX(LocalDate date) {
    return LEFT_MARGIN + PPH * (date.getYear() - TZERO) + PPM * date.getMonthValue();
  }

  private LocalDate xToDate(int x) {
    x -= LEFT_MARGIN;
    int y = Math.max(0, (x / PPH) + TZERO);
    int m = Math.max(0, (x % PPH) / PPM);
    return LocalDate.of(y, m, m);
  }

  private int roomToY(int room) {
    return TOP_MARGIN + (ROW_HEIGHT * (room - 1));
  }

  private int yToRoom(int y) {
    return ((y - TOP_MARGIN) / ROW_HEIGHT) + 1;
  }

  // Data members

  private Frame         parentFrame;
  private BookingSystem bs;
  private Image         offscreen;
  private List<Room>    roomNumbers;
  private int           firstX, firstY, currentX, currentY;
  private boolean       mouseDown;

  public StaffUI(Frame f) {
    parentFrame = f;

    roomNumbers = BookingSystem. getRooms();
    setSize(LEFT_MARGIN + (SLOTS * COL_WIDTH), TOP_MARGIN +roomNumbers.size() * ROW_HEIGHT + BOTTOM_MARGIN);
    setBackground(Color.white);
    bs = BookingSystem.getInstance();
    bs.addObserver(this);
    bs.setMonth(LocalDateTime.now().getMonth());

    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        currentX = firstX = e.getX();
        currentY = firstY = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
          mouseDown = true;
          bs.selectBooking(yToRoom(firstY), xToDate(firstX));
        }
      }
    });
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        Booking b = bs.getSelectedBooking();
        if (b != null) {
          bs.changeSelected(xToDate(dateToX(b.getDate()) + currentX - firstX), yToRoom(currentY));
        }
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        currentX = e.getX();
        currentY = e.getY();
        update();
      }
    });
  }

  public void update() {
    repaint();
  }

  public void paint(Graphics g) {
    update(g);
  }

  public void update(Graphics g) {
    Dimension canvasSize = getSize();
    if (offscreen == null) {
      offscreen = this.createImage(canvasSize.width, canvasSize.height);
    }
    Graphics og = offscreen.getGraphics();
    og.setColor(getBackground());
    og.fillRect(0, 0, canvasSize.width, canvasSize.height);
    og.setColor(Color.black);

    // Draw screen outlines

    og.drawLine(LEFT_MARGIN, 0, LEFT_MARGIN, canvasSize.height);
    og.drawLine(0, TOP_MARGIN, canvasSize.width, TOP_MARGIN);

    // Write table numbers and horizontal rules

    for (int i = 0; i < roomNumbers.size(); i++) {
      int y = TOP_MARGIN + (i + 1) * ROW_HEIGHT;
      og.drawString(roomNumbers.get(i).getNumber() + " (" + roomNumbers.get(i).getSize() + ")", 0, y);
      og.drawLine(LEFT_MARGIN, y, canvasSize.width, y);
    }

    // Write time labels and vertical rules******************************

    for (int i = 0; i < SLOTS; i++) {
      String tmp = (TZERO + i) + ("");
      
      int x = LEFT_MARGIN + i * COL_WIDTH;
      og.drawString(tmp, x, 40);
      og.drawLine(x, TOP_MARGIN, x, canvasSize.height - BOTTOM_MARGIN);
    }

    // Display booking information

    og.drawString(bs.getMonth().toString(), LEFT_MARGIN, 20);

    List<Booking> enumV = bs.getBookings();
    for (Booking b : enumV) {
      int x = dateToX(b.getDate());
      int y = roomToY(b.getRoom().getNumber());
      og.setColor(Color.gray);
      og.fillRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
      if (b == bs.getSelectedBooking()) {
        og.setColor(Color.red);
        og.drawRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
      }
      og.setColor(Color.white);
      og.drawString(b.getDetails(), x, y + ROW_HEIGHT / 2);
    }

    // Draw an outline to represent position of dragged booking

    Booking b = bs.getSelectedBooking();
    if (mouseDown && b != null) {
      int x = dateToX(b.getDate()) + currentX - firstX;
      int y = roomToY(b.getRoom().getNumber()) + currentY - firstY;
      og.setColor(Color.red);
      og.drawRect(x, y, 4 * COL_WIDTH, ROW_HEIGHT);
    }

    // Write to canvas

    g.drawImage(offscreen, 0, 0, this);
  }

  public boolean message(String message, boolean confirm) {
    ConfirmDialog d = new ConfirmDialog(parentFrame, message, confirm);
    d.show();
    return d.isConfirmed();
  }

  void displayDate() {
    DateDialog d = new DateDialog(parentFrame, "Enter a date");
    d.show();
    if (d.isConfirmed()) {
      LocalDate month = d.getMonth();
      bs.setMonth(month.getMonth());
    }
  }

  void addReservation() {
    ReservationDialog d = new ReservationDialog(parentFrame, "Enter reservation details");
    d.show();
    if (d.isConfirmed()) {
      bs.addReservation(d.getCovers(), bs.getMonth(), d.getDate(), d.getRoomNumber(), d.getCustomerName(), d.getPhoneNumber());
    }
  }

  void addWalkIn() {
    WalkInDialog d = new WalkInDialog(parentFrame, "Enter walk-in details");
    d.show();
    if (d.isConfirmed()) {
      bs.addWalkIn(d.getCovers(), bs.getMonth(), d.getDate(), d.getRoomNumber());
    }
  }

  void cancel() {
    bs.cancelSelected();
  }

  void recordArrival() {
    bs.recordArrival(LocalDate.now());
  }
}

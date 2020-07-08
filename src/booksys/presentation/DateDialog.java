
package booksys.presentation;

import java.awt.*;
import java.awt.event.*;
import java.sql.Date;
import java.time.LocalDate;

class DateDialog extends Dialog {
  TextField tf;
  boolean   confirmed;

  DateDialog(Frame owner, String title) {
    super(owner, title, true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        DateDialog.this.hide();
      }
    });

    Label promptLabel = new Label("Enter Date:", Label.RIGHT);

    tf = new TextField("YYYY-MM-DD", 10);

    Button confirm = new Button("OK");
    confirm.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = true;
        DateDialog.this.hide();
      }
    });

    Button cancel = new Button("Cancel");
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmed = false;
        DateDialog.this.hide();
      }
    });

    setLayout(new GridLayout(0, 2));

    add(promptLabel);
    add(tf);

    add(confirm);
    add(cancel);

    pack();
  }

  boolean isConfirmed() {
    return confirmed;
  }

  LocalDate getMonth() {
    return LocalDate.parse(tf.getText());
  }
}

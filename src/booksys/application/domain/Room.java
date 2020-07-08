package booksys.application.domain;

public class Room {
	  private int number;
	  private int size;

	  public Room(int n, int s) {
	    number = n;
	    size = s;
	  }

	  public int getNumber() {
	    return number;
	  }

	  public int getSize() {
	    return size;
	  }
	}

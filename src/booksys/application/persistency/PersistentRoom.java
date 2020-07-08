package booksys.application.persistency;


import booksys.application.domain.Room;

class PersistentRoom extends Room {
  private int oid;

  PersistentRoom(int id, int n, int c) {
    super(n, c);
    oid = id;
  }

  int getId() {
    return oid;
  }

public int getNumber() {
	// TODO Auto-generated method stub
	return 0;
}
}
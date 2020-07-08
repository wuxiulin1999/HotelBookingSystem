package booksys.application.persistency;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import booksys.application.domain.Room;
import booksys.storage.Database;

public class RoomMapper {
  // Implementation of hidden cache

  private Map<Integer, PersistentRoom> cache;

  private PersistentRoom getFromCache(int oid) {
    return cache.get(oid);
  }

  private PersistentRoom getFromCacheByNumber(int rno) {
    for (PersistentRoom r : cache.values()) {
      if (r.getNumber() == rno) {
        return r;
      }
    }
    return null;
  }

  private void addToCache(PersistentRoom r) {
    cache.put(r.getId(), r);
  }

  // Constructor:

  private RoomMapper() {
    cache = new HashMap<Integer, PersistentRoom>();
    getRooms();
  }

  // Singleton:

  private static RoomMapper uniqueInstance;

  public static RoomMapper getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new RoomMapper();
    }
    return uniqueInstance;
  }

  public PersistentRoom getRoom(int rno) {
    PersistentRoom r = getFromCacheByNumber(rno);
    return r;
  }

  PersistentRoom getRoomForOid(int oid) {
    PersistentRoom r = (PersistentRoom) getFromCache(oid);
    return r;
  }

  public List<Room> getRooms() {
    if (cache.size() == 0) {

      List<Room> v = new ArrayList<Room>();
      try {
        Database.getInstance();
        Statement stmt = Database.getConnection().createStatement();
        ResultSet rset = stmt.executeQuery("SELECT ROWID, number, size FROM Room ORDER BY number");
        while (rset.next()) {
          PersistentRoom r = new PersistentRoom(rset.getInt("ROWID"), rset.getInt("number"), rset.getInt("size"));
          v.add(r);
          addToCache(r);
        }
        rset.close();
        stmt.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return v;
    } else {
      return new ArrayList<Room>(cache.values());
    }
  }

}

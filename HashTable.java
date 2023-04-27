/**
 * Simple HashTable class.
 * 
 * @author CS240 Instructors and ...
 *
 */
public class HashTable<K, V> {

  public static final int INITIAL_CAPACITY = 16; // must be a power of 2.
  public static final double MAX_LOAD = .5;

  Item[] table; // (Leave this non-private for testing.)
  private int size;

  /**
   * HashTable constructor.
   */
  public HashTable() {
    table = new HashTable.Item[INITIAL_CAPACITY];
    size = 0;
  }


  /**
   * Store the provided key/value pair.
   */
  public void put(K key, V value) {
    if (size + 1 > MAX_LOAD * table.length) {
      resize();
    }
    int index = findKey(key);
    if (index != -1) {
      table[index].setValue(value);
      return;
    }
    index = indexFor(key.hashCode(), table.length);
    Item currItem;
    while (true) {
      currItem = table[index];
      if (currItem == null) {
        table[index] = new Item(key, value);;
        size++;
        return;
      } else if (currItem.isDeleted()) {
        table[index].setValue(value);
        return;
      }
      index = (index + 1) % table.length;
    }
  }

  /**
   * Return the value associated with the provided key, or null if no such value
   * exists.
   */
  public V get(K key) {
    int keyIndex = findKey(key);
    if (keyIndex != -1) {
      return table[keyIndex].value();
    }
    return null;
  }

  /**
   * Remove the provided key from the hash table and return the associated value.
   * Returns null if the key is not stored in the table.
   */
  public V remove(K key) {
    int keyIndex = findKey(key);
    if (keyIndex != -1) {
      V retVal = table[keyIndex].value;
      table[keyIndex].delete();
      size--;
      return retVal;
    }
    return null;
  }

  /**
   * Return the number of items stored in the table.
   */
  public int size() {
    return size;
  }

  // PRIVATE HELPER METHODS BELOW THIS POINT----------


  /**
   * Double the size of the hash table and rehash all existing items.
   */
  private void resize() {
    Item[] oldTable = table;
    table = new HashTable.Item[(table.length * 2)];
    for (Item item : oldTable) {
      if (item == null ||  item.isDeleted()) {
        continue;
      }
      putResize(item);
    }
  }

  private void putResize(Item item) {
    int index = indexFor(item.key().hashCode(), table.length);

    if (table[index] != null) {
      while (true) {
        index = (index + 1) % table.length;
        if (table[index] != null) {
          table[index] = item;
          return;
        }
      }
    } else {
      table[index] = item;
    }
  }


  /**
   * Find the index of a key or return -1 if it can't be found. If removal is
   * implemented, this will skip over tombstone positions during the search.
   */
  private int findKey(K key) {
    int index = indexFor(key.hashCode(), table.length);
    while (true) {
      Item item = table[index];
      if (item == null) {
        return -1;
      } else if (item.key().equals(key)) {
        return index;
      }
      index = (index + 1) % table.length;
    }
  }


  /**
   * Returns index for hash code h.
   */
  private int indexFor(int h, int length) {
    return hash(h) & (length - 1);
  }

  /**
   * Applies a supplemental hash function to a given hashCode, which defends
   * against poor quality hash functions. This is critical because HashMap uses
   * power-of-two length hash tables, that otherwise encounter collisions for
   * hashCodes that do not differ in lower bits.
   */
  private int hash(int h) {
    // This function ensures that hashCodes that differ only by
    // constant multiples at each bit position have a bounded
    // number of collisions (approximately 8 at default load factor).
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }

  /**
   * Item class is a simple wrapper for key/value pairs.
   */
  class Item { // leave this non-private for testing.
    private K key;
    private V value;
    private boolean tombstone;

    /**
     * Create an Item object.
     */
    public Item(K key, V value) {
      this.key = key;
      this.value = value;
      this.tombstone = false;
    }
    
    /* Getters and setters */
    public K key() {
      return key;
    }

    public V value() {
      return value;
    }

    public void setValue(V value) {
      this.value = value;
    }

    public boolean isDeleted() {
      return tombstone;
    }

    public void delete() {
      tombstone = true;
    }
  }

}

// The hash and indexFor methods are taken directly from the Java HashMap
// implementation with some modifications. That code is licensed as follows:
/*
 * Copyright 1997-2007 Sun Microsystems, Inc. All Rights Reserved. DO NOT ALTER
 * OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 only, as published by
 * the Free Software Foundation. Sun designates this particular file as subject
 * to the "Classpath" exception as provided by Sun in the LICENSE file that
 * accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License version 2 for more
 * details (a copy is included in the LICENSE file that accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this work; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, CA
 * 95054 USA or visit www.sun.com if you need additional information or have any
 * questions.
 */



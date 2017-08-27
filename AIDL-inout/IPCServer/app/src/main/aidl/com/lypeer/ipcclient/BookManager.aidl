// BookManager.aidl
package com.lypeer.ipcclient;
import com.lypeer.ipcclient.Book;

interface BookManager {

    List<Book> getBooks();
    //void addBook(inout Book book);

    Book addBookIn(in Book book);
    Book addBookOut(out Book book);
    Book addBookInout(inout Book book);
}

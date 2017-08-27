// BookManager.aidl
package com.lypeer.ipcclient;
import com.lypeer.ipcclient.Book;

interface BookManager {

    //保证客户端与服务端是连接上的且数据传输正常
    List<Book> getBooks();
    //void addBook(inout Book book);

//    通过三种定位tag做对比试验，观察输出的结果
    Book addBookIn(in Book book);
    Book addBookOut(out Book book);
    Book addBookInout(inout Book book);
}

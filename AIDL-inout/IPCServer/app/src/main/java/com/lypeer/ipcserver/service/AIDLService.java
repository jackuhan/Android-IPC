package com.lypeer.ipcserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lypeer.ipcclient.Book;
import com.lypeer.ipcclient.BookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端的AIDLService.java
 * <p/>
 * Created by lypeer on 2016/7/17.
 */
public class AIDLService extends Service {

    public final String TAG = this.getClass().getSimpleName();

    //包含Book对象的list
    private List<Book> mBooks = new ArrayList<>();

    //由AIDL文件生成的BookManager
    private final BookManager.Stub mBookManager = new BookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBooks() method , now the list is : " + mBooks.toString());
                if (mBooks != null) {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }


        //@Override
        //public void addBook(Book book) throws RemoteException {
        //    synchronized (this) {
        //        if (mBooks == null) {
        //            mBooks = new ArrayList<>();
        //        }
        //        if (book == null) {
        //            Log.e(TAG, "Book is null in In");
        //            book = new Book();
        //        }
        //        //尝试修改book的参数，主要是为了观察其到客户端的反馈
        //        book.setPrice(2333);
        //        if (!mBooks.contains(book)) {
        //            mBooks.add(book);
        //        }
        //        //打印mBooks列表，观察客户端传过来的值
        //        Log.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
        //    }
        //}

        @Override
        public Book addBookIn(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if(book == null){
                    Log.e(TAG , "Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setName("server addBookIn");
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                //打印mBooks列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
                return book;
            }
        }

        @Override
        public Book addBookOut(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if(book == null){
                    Log.e(TAG , "Book is null in Out");
                    book = new Book();
                }
                book.setName("server addBookOut");
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                Log.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
                return book;
            }
        }

        @Override
        public Book addBookInout(Book book) throws RemoteException {
            synchronized (this) {
                if (mBooks == null) {
                    mBooks = new ArrayList<>();
                }
                if(book == null){
                    Log.e(TAG , "Book is null in Inout");
                    book = new Book();
                }
                book.setName("server addBookInout");
                book.setPrice(2333);
                if (!mBooks.contains(book)) {
                    mBooks.add(book);
                }
                Log.e(TAG, "invoking addBooks() method , now the list is : " + mBooks.toString());
                return book;
            }
        }
    };

    @Override
    public void onCreate() {
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(), String.format("on bind,intent = %s", intent.toString()));
        return mBookManager;
    }
}

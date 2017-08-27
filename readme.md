demo中开启多进程的方式有两种。
1、在manifest中注册service加上属性android:process=":xxx"
2、使用不同apk。在5.0以上手机中需要设置服务端的service为android:exported="true",
并且在客户端打bind远程service的时候需要加上服务端的packagename,intent.setPackage("com.lypeer.ipcserver");

在AIDL中客户端和服务端都使用BookManager.aidl需要对其两端代码,双方进程并不知道对方的具体方法,而是遵守约定,按照顺序或获取对应方法。
比如客户端中代码如下,服务端如果缺少一个方法,将会导致客户端的addBookIn调用到服务端的addBookOut。

interface BookManager {
          //保证客户端与服务端是连接上的且数据传输正常
          List<Book> getBooks();
      //    通过三种定位tag做对比试验，观察输出的结果
          Book addBookIn(in Book book);
          Book addBookOut(out Book book);
          Book addBookInout(inout Book book);
      }
      

interface BookManager {
          //保证客户端与服务端是连接上的且数据传输正常
          List<Book> getBooks();
      //    通过三种定位tag做对比试验，观察输出的结果
      //    Book addBookIn(in Book book);
          Book addBookOut(out Book book);
          Book addBookInout(inout Book book);
      }




## AIDLDemo-inout
对应博客：你真的理解AIDL中的in，out，inout么？
http://blog.csdn.net/luoyanglizi



## AIDL-callbacks
linkToDeath
Binder类提供linkToDeath方法在客户端可以设置死亡代理，当服务端的Binder对象“死亡”，客户端可以受到死亡通知，这个时候我们可以重新恢复链接。
RemoteCallbackList 异步回调方式

## IPC_Messenger
Messenger，双方都用Messenger.send即可
### 客户端
msgFromClient.replyTo = mMessenger;
mService.send(msgFromClient);
### 服务端
msgfromClient.replyTo.send(msgToClient);



## HermesEventBus
可以跨进程的Eventbus



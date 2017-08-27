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


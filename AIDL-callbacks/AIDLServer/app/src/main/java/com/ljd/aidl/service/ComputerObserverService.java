package com.ljd.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.ljd.aidl.IComputerManagerObserver;
import com.ljd.aidl.IOnComputerArrivedListener;
import com.ljd.aidl.entity.ComputerEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComputerObserverService extends Service{
    public ComputerObserverService() {
    }

    private CopyOnWriteArrayList<ComputerEntity> mComputerList = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnComputerArrivedListener> mComputerArrivedListenerList = new RemoteCallbackList<>();
    private AtomicBoolean mIsServiceDestroy = new AtomicBoolean(false);

    private Binder mBinder = new IComputerManagerObserver.Stub(){

        @Override
        public void addComputer(ComputerEntity computer) throws RemoteException {
            mComputerList.add(computer);
        }

        @Override
        public List<ComputerEntity> getComputerList() throws RemoteException {
            return mComputerList;
        }

        @Override
        public void registerUser(IOnComputerArrivedListener listener) throws RemoteException {
            mComputerArrivedListenerList.register(listener);
        }

        @Override
        public void unRegisterUser(IOnComputerArrivedListener listener) throws RemoteException {
            mComputerArrivedListenerList.unregister(listener);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mComputerList.add(new ComputerEntity(0,"apple","macbookpro"));
        mComputerList.add(new ComputerEntity(1,"microsoft","surfacebook"));
        mComputerList.add(new ComputerEntity(2,"dell","XPS13"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsServiceDestroy.get()){
                    try {
                        Thread.currentThread().sleep(3000);
                        ComputerEntity computer = new ComputerEntity(mComputerList.size(),"******","******");
                        mComputerList.add(computer);
                        final int COUNT = mComputerArrivedListenerList.beginBroadcast();
                        //通知所有注册过的用户
                        for (int i=0;i<COUNT;i++){
                            IOnComputerArrivedListener listener = mComputerArrivedListenerList.getBroadcastItem(i);
                            if (listener != null){
                                listener.onComputerArrived(computer);
                            }
                        }
                        mComputerArrivedListenerList.finishBroadcast();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsServiceDestroy.set(true);
    }
}

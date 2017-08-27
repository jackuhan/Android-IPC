package com.ljd.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.ljd.aidl.IComputerManager;
import com.ljd.aidl.entity.ComputerEntity;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ComputerService extends Service {
    private CopyOnWriteArrayList<ComputerEntity> mComputerList = new CopyOnWriteArrayList<>();

    public ComputerService() {
    }

    private final IComputerManager.Stub mBinder = new IComputerManager.Stub() {
        @Override
        public void addComputer(ComputerEntity computer) throws RemoteException {
            mComputerList.add(computer);
        }

        @Override
        public List<ComputerEntity> getComputerList() throws RemoteException {
            return mComputerList;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mComputerList.add(new ComputerEntity(0,"apple","macbookpro"));
        mComputerList.add(new ComputerEntity(1,"microsoft","surfacebook"));
        mComputerList.add(new ComputerEntity(2,"dell","XPS13"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}

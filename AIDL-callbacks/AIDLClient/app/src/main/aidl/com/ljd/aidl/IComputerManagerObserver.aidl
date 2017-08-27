// IComputerManagerObserver.aidl
package com.ljd.aidl;

import com.ljd.aidl.entity.ComputerEntity;
import com.ljd.aidl.IOnComputerArrivedListener;
// Declare any non-default types here with import statements

interface IComputerManagerObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void addComputer(in ComputerEntity computer);
     List<ComputerEntity> getComputerList();
     void registerUser(IOnComputerArrivedListener listener);
     void unRegisterUser(IOnComputerArrivedListener listener);
}

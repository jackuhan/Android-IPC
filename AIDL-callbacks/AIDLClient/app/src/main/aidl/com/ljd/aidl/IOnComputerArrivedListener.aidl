// IOnComputerArrivedListener.aidl
package com.ljd.aidl;

import com.ljd.aidl.entity.ComputerEntity;
// Declare any non-default types here with import statements

interface IOnComputerArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onComputerArrived(in ComputerEntity computer);
}

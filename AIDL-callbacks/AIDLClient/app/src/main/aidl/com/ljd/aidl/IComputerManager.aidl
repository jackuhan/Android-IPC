// IComputerManager.aidl
package com.ljd.aidl;

import com.ljd.aidl.entity.ComputerEntity;
// Declare any non-default types here with import statements

interface IComputerManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void addComputer(in ComputerEntity computer);
     List<ComputerEntity> getComputerList();
}

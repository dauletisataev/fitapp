package com.example.android.myhealth.miBandHelper.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * Это чисто статичные данные Bluetooth Manager
 */
public class BM {
    private static BluetoothAdapter adapter;
    private static BluetoothGatt bluetoothGatt;
    private static MyBluetoothGattCallBack callBack;

    public static BluetoothAdapter getAdapter() {
        if (adapter != null) return adapter;
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public static void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        BM.bluetoothGatt = bluetoothGatt;
    }

    public static MyBluetoothGattCallBack getCallback() {
        return callBack;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void initializeCallBack(MiBand miBand) {
        callBack = new MyBluetoothGattCallBack(miBand);
    }
}

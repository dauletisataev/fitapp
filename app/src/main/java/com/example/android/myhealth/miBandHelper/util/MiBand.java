package com.example.android.myhealth.miBandHelper.util;

import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;

import java.util.Observable;

public class MiBand extends Observable implements Parcelable{

    private BluetoothDevice bluetoothDevice;
    private BatteryInfo batteryInfo;
    private int heartRate;

    public MiBand() {
        heartRate = 0;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    // Отсюда и до конца класса идет муть для реализации интерфейса Parcelable
    public static final Creator<MiBand> CREATOR = new Creator<MiBand>() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public MiBand createFromParcel(Parcel in) {
            return new MiBand(in);
        }

        @Override
        public MiBand[] newArray(int size) {
            return new MiBand[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(bluetoothDevice);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private MiBand(Parcel parcel) {
        this.bluetoothDevice = (BluetoothDevice) parcel.readValue(BluetoothDevice.class.getClassLoader());
    }

    public BatteryInfo getBatteryInfo() {
        return batteryInfo;
    }

    public void setBatteryInfo(BatteryInfo batteryInfo) {
        this.batteryInfo = batteryInfo;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }
}

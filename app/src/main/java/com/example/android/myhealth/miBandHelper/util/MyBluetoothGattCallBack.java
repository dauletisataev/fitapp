package com.example.android.myhealth.miBandHelper.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MyBluetoothGattCallBack extends BluetoothGattCallback {
    private MiBand miBand;
    private BluetoothGatt mGatt;


    public MyBluetoothGattCallBack(MiBand miBand) {
        this.miBand = miBand;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        switch (newState) {
            case BluetoothProfile.STATE_CONNECTED:
                gatt.discoverServices();
                break;
            case BluetoothProfile.HID_DEVICE:
                gatt.close();
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        if (status == BluetoothGatt.GATT_SUCCESS) {
            mGatt = gatt;
        }
    }

    public void writeCharacteristic(UUID serviceUUID, UUID characteristicId, byte[] value) {
        BluetoothGattService service = mGatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicId);
            if (characteristic != null) {
                characteristic.setValue(value);
                if (mGatt.writeCharacteristic(characteristic)) {
                    System.out.println("******************SUCCESS**********************");
                }
            }
        }
    }

    public void readCharacteristic(UUID serviceUUID, UUID characteristicId) {

        BluetoothGattService service = mGatt.getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicId);
            if (characteristic != null) {
                if (mGatt.readCharacteristic(characteristic)) {
                    System.out.println("*****************READ SUCCESS******************");
                }
            }
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);

        if (BluetoothGatt.GATT_SUCCESS == status) {
            System.out.println("onCharacteristicRead NORMAL");
            notifyWithResult(characteristic);
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (BluetoothGatt.GATT_SUCCESS == status) {
            notifyWithResult(characteristic);
        }
    }

    public void setNotifyListener(UUID serviceUUID, UUID characteristicId) {

        BluetoothGattService service = BM.getBluetoothGatt().getService(serviceUUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(characteristicId);
            if (characteristic != null) {
                BM.getBluetoothGatt().setCharacteristicNotification(characteristic, true);
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(Profile.UUID_DESCRIPTOR_UPDATE_NOTIFICATION);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                BM.getBluetoothGatt().writeDescriptor(descriptor);
            }
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        notifyWithResult(characteristic);
    }

    private void notifyWithResult(BluetoothGattCharacteristic data) {
        if (data.getService().getUuid().equals(Profile.UUID_SERVICE_HEARTRATE)) {
            if (data.getUuid().equals(Profile.UUID_NOTIFICATION_HEARTRATE)) {
                if (data.getValue().length == 2 && data.getValue()[0] == 6) {
                    System.out.println("Инфо от пульса ЕСТЬ = " + (data.getValue()[0]) + " " + data.getValue()[1]);
                    miBand.setHeartRate(data.getValue()[1] & 0xFF);
                }
            }
        }
        if (data.getService().getUuid().equals(Profile.UUID_SERVICE_MILI)) {
            if (data.getUuid().equals(Profile.UUID_CHAR_BATTERY)) {
                miBand.setBatteryInfo(BatteryInfo.fromByteData(data.getValue()));
            }
        }
    }

    public MiBand getMiBand() {
        return miBand;
    }
}

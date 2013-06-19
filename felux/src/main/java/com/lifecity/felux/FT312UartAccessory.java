package com.lifecity.felux;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

public class FT312UartAccessory
{
	private static final String TAG = "FT312UartAccessory";
	public ParcelFileDescriptor fileDescriptor = null;
	public FileInputStream inputStream = null;
	public FileOutputStream outputStream = null;

    public static final byte DATA_BITS_7 = (byte)7;
    public static final byte DATA_BITS_8 = (byte)8;

    public static final byte PARITY_NONE = (byte)0;
    public static final byte PARITY_ODD = (byte)1;
    public static final byte PARITY_EVEN = (byte)2;
    public static final byte PARITY_MARK = (byte)3;
    public static final byte PARITY_SPACE = (byte)4;

    public static final byte STOP_BITS_1 = (byte)1;
    public static final byte STOP_BITS_2 = (byte)2;

    public static final byte FLOW_CONTROL_NONE = (byte)0;
    public static final byte FLOW_CONTROL_CTS_RTS = (byte)1;

	private byte [] writeBuffer = new byte[256];

    public void reset() throws IOException  {
        writeBuffer[0] = (byte)64;
        writeUsb(1);
    }

	public void setConfig(int baudRate, byte dataBits, byte stopBits, byte parity,
			byte flowControl) throws IOException {
		writeBuffer[0] = (byte)baudRate;
		writeBuffer[1] = (byte)(baudRate >> 8);
		writeBuffer[2] = (byte)(baudRate >> 16);
		writeBuffer[3] = (byte)(baudRate >> 24);

		writeBuffer[4] = dataBits;
		writeBuffer[5] = stopBits;
        writeBuffer[6] = parity;
		writeBuffer[7] = flowControl;

		writeUsb((int)8);
	}

	public void write(byte[] buffer) throws IOException {
		write(buffer, 0, buffer.length);
	}

	public void write(byte[] buffer, int count) throws IOException {
		write(buffer, 0, count);
	}

	public void write(byte[] buffer, int offset, int count) throws IOException {
		count = Math.min(buffer.length, count);
		count = Math.min(256, count);

		if(offset < count && count > 0) {
			for(int i = offset; i < count; i++)
			{
				writeBuffer[i] = buffer[i];
			}

            if(count != 64)
            {
                writeUsb(count);
            }
            else
            {
                byte temp = writeBuffer[63];
                writeUsb(63);
                writeBuffer[0] = temp;
                writeUsb(1);
            }
        }
	}

	private void writeUsb(int count) throws IOException {
        if(outputStream != null){
            outputStream.write(writeBuffer, 0, count);
        }
	}

	public boolean open(UsbManager manager, UsbAccessory accessory) throws IOException {
        if (accessory != null) {
            //close();
            if (fileDescriptor == null && inputStream == null && outputStream == null) {
                fileDescriptor = manager.openAccessory(accessory);

                if (fileDescriptor != null) {
                    FileDescriptor fd = fileDescriptor.getFileDescriptor();
                    inputStream = new FileInputStream(fd);
                    outputStream = new FileOutputStream(fd);

                    return true;
                }
            }
        }

        return false;
	}

    public void close() throws IOException {
        if(fileDescriptor != null) {
            fileDescriptor.close();
            fileDescriptor = null;
        }

        if(inputStream != null) {
            inputStream.close();
            inputStream = null;
        }

        if(outputStream != null) {
            outputStream.close();
            outputStream = null;
        }
    }
}

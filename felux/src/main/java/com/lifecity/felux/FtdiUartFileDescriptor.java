package com.lifecity.felux;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.ParcelFileDescriptor;

public class FtdiUartFileDescriptor extends ParcelFileDescriptor
{
    private static final String TAG = "FtdiUartFileDescriptor";

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

    public FtdiUartFileDescriptor(ParcelFileDescriptor fileDescriptor) {
        super(fileDescriptor);
    }

    public class FtdiUartInputStream extends AutoCloseInputStream {
        public FtdiUartInputStream(ParcelFileDescriptor fd) {
            super(fd);
        }
    }

    public class FtdiUartOutputStream extends AutoCloseOutputStream {

        public FtdiUartOutputStream(ParcelFileDescriptor fd) {
            super(fd);
        }

        public void setConfig(int baudRate, byte dataBits, byte stopBits, byte parity,
                byte flowControl) throws IOException {
            byte[] buffer = new byte[8];
            buffer[0] = (byte)baudRate;
            buffer[1] = (byte)(baudRate >> 8);
            buffer[2] = (byte)(baudRate >> 16);
            buffer[3] = (byte)(baudRate >> 24);

            buffer[4] = dataBits;
            buffer[5] = stopBits;
            buffer[6] = parity;
            buffer[7] = flowControl;

            super.write(buffer);
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            count = Math.min(buffer.length, count);
            count = Math.min(256, count);

            if(offset < count && count > 0) {
                if(count != 64)
                {
                    super.write(buffer, offset, count);
                }
                else
                {
                    super.write(buffer, offset, count - 1);
                    super.write(buffer[offset + count - 1]);
                }
            }
        }
    }
}

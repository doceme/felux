package com.lifecity.felux;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import static com.lifecity.felux.SimpleHdlcInputStreamReader.HdlcReadState.*;

public class SimpleHdlcInputStreamReader {
    protected static final String TAG = "SimpleHdlcInputStreamReader";
    protected static final int FRAME_BYTE = 0x7e;
    protected static final int ESCAPE_BYTE = 0x7d;
    protected static final int TOGGLE_BYTE = (1 << 5);

    InputStream in;
    CRC32 crc = new CRC32();

    protected static enum HdlcReadState {
        START,
        LEN1,
        LEN2,
        DATA,
        CRC1,
        CRC2,
        CRC3,
        CRC4
    };

    public SimpleHdlcInputStreamReader(InputStream in) {
        if (in == null) {
            throw new NullPointerException("InputStream must not be null");
        }

        this.in = in;
    }

    public void close() throws IOException {
        if (in != null) {
            in.close();
        }
    }

    public int read(byte[] buffer, int offset, int count) throws IOException {
        int index = offset;
        int crcValue = 0;
        int bytesRead = 0;
        int length = 0;
        boolean done = false;
        boolean escaped = false;
        HdlcReadState state = START;

        while (!done && (index - offset) < count) {
            byte data = (byte)in.read();

            /* Check for start or escape byte */
            if (data == FRAME_BYTE) {
                state = START;
            } else if (data == ESCAPE_BYTE) {
                escaped = true;
                continue;
            }

            /* Handle escaped byte */
            if (escaped) {
                data ^= TOGGLE_BYTE;
                escaped = false;
            }

            switch (state) {
                case START:
                    index = offset;
                    state = LEN1;
                    escaped = false;
                    crc.reset();
                    break;
                case LEN1:
                    length = ((int)data << 8);
                    state = LEN2;
                    break;
                case LEN2:
                    length |= data;
                    state = DATA;
                    break;
                case DATA:
                    buffer[index++] = data;
                    crc.update(data);
                    if (index == length) {
                        state = CRC1;
                    }
                    break;
                case CRC1:
                    crcValue = ((int)data << 24);
                    state = CRC2;
                    break;
                case CRC2:
                    crcValue |= ((int)data << 16);
                    state = CRC3;
                    break;
                case CRC3:
                    crcValue |= ((int)data << 8);
                    state = CRC4;
                    break;
                case CRC4:
                    crcValue |= data;
                    state = START;
                    if (crcValue == crc.getValue()) {
                        done = true;
                    } else {
                        Log.e(TAG, "Bad CRC packet: calculated crc=0x" + Integer.toHexString((int)crc.getValue()) + " read crc=0x" + Integer.toHexString(crcValue));
                    }
                    break;
            }
        }

        return index;
    }
}

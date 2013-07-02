package com.lifecity.felux;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;

public class SimpleHdlcOutputStreamWriter {
    protected static final int FRAME_BYTE = 0x7e;
    protected static final int ESCAPE_BYTE = 0x7d;
    protected static final int TOGGLE_BYTE = (1 << 5);

    OutputStream out;
    CRC32 crc = new CRC32();

    public SimpleHdlcOutputStreamWriter(OutputStream out) {
        if (out == null) {
            throw new NullPointerException("OutputStream must not be null");
        }

        this.out = out;
    }

    public void close() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    private void write_escape(int data) throws IOException {
        if (data == FRAME_BYTE || data == ESCAPE_BYTE) {
            out.write(ESCAPE_BYTE);
            out.write(data ^ TOGGLE_BYTE);
        } else {
            out.write(data);
        }
    }

    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
        if (offset > buffer.length || offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset out of bounds: " + offset);
        }
        if (count < 0 || count > buffer.length - offset) {
            throw new ArrayIndexOutOfBoundsException("Length out of bounds: " + count);
        }

        /* Start of frame */
        out.write(FRAME_BYTE);

        /* Payload size */
        write_escape((count >> 8) & 0xff);
        write_escape(count & 0xff);

        crc.reset();

        for (int i = offset; i < count; i++) {
            byte data = buffer[i];
            write_escape(data);
            crc.update(data);
        }

        /* CRC */
        int crcValue = (int)crc.getValue();

        write_escape(((crcValue >> 24) & 0xff));
        write_escape(((crcValue >> 16) & 0xff));
        write_escape(((crcValue >> 8) & 0xff));
        write_escape((crcValue & 0xff));
        out.flush();
    }

    public void write(int data) throws IOException {
        byte[] buffer = new byte[1];
        buffer[0] = (byte)data;
        write(buffer);
    }
}

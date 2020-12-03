package com.dhao.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Create by DiaoHao on 2020/12/2 21:09
 */
public class GzipCompress implements Compress{
    private static final int BUFFER_SIZE = 1024 * 4;

    @Override
    public byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("GzipCompress compress byte is null");
        }
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream stream = new GZIPOutputStream(out);
            stream.write(bytes);
            stream.flush();
            stream.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error: ", e);
        }
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("GzipCompress decompress byte is null");
        }
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error: ", e);
        }
    }
}

package com.dhao.compress;

public interface Compress {

    /**
     * 压缩数据
     * @param bytes 要压缩的数据
     * @return 压缩之后的数据
     */
    byte[] compress(byte[] bytes);

    /**
     * 解压数据
     * @param bytes 要解压的数据
     * @return 解压之后的数据
     */
    byte[] decompress(byte[] bytes);
}

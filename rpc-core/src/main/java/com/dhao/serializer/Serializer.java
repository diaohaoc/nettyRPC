package com.dhao.serializer;

public interface Serializer {

    /**
     * 序列化消息
     *
     * @param object 要序列化的对象
     * @return 序列化之后的字节数组
     */
    public byte[] serialize(Object object);

    /**
     * 反序列化
     * @param bytes 序列化之后的数组
     * @param clazz 要序列化成的类
     * @param <T> 类的类型
     * @return 反序列化的对象
     */
    public <T> T deserialize(byte[] bytes, Class<T> clazz);

}

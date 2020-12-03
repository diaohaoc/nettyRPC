package com.dhao.serializer;

import com.dhao.exception.SerializeException;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

/**
 * Create by DiaoHao on 2020/11/28 20:20
 */
public class KryoSerializer implements Serializer{
    //Kryo是非线程安全的，需要通过ThreadLocal保证线程安全
    final ThreadLocal<Kryo> threadLocal = ThreadLocal.withInitial(Kryo::new);

    public byte[] serialize(Object object) {
        try{
            Output output = new Output(new ByteArrayOutputStream());
            Kryo kryo = threadLocal.get();
            kryo.writeObject(output, object);
            threadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("Serialization failed");
        }

    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try{
            Input input = new Input(new ByteArrayInputStream(bytes));
            Kryo kryo = threadLocal.get();
            Object object = kryo.readObject(input, clazz);
            threadLocal.remove();
            return clazz.cast(object);
        } catch (Exception e) {
            throw new SerializeException("Deserialization failed");
        }
    }

//    public static void main(String[] args) {
//        String msg = "你好，世界hello";
//        byte[] bytes = new KryoSerializer().serialize(msg);
//        String resmes = new KryoSerializer().deserialize(bytes, String.class);
//        System.out.println(resmes);
//    }
}

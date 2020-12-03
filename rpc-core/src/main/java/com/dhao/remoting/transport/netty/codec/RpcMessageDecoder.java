package com.dhao.remoting.transport.netty.codec;

import com.dhao.compress.Compress;
import com.dhao.compress.GzipCompress;
import com.dhao.remoting.constants.RpcConstants;
import com.dhao.remoting.dto.RpcMessage;
import com.dhao.remoting.dto.RpcRequest;
import com.dhao.serializer.KryoSerializer;
import com.dhao.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * 消息解码器
 * 0            4         5              9              10            14
 * +---+---+---+---------+----+----+-----+--------------+-----+--------+
 * |magic code | version | full length   |messageType  | RequestId    |
 * ---------------------------------------------------------------------
 * |                                                                    |
 * |                                                                    |
 * |                                      body                          |
 * |                                                                    |
 * ---------------------------------------------------------------------
 * 4B magic code(魔数)  1B version(版本)  4B full length(数据包的长度) 1B messageType(消息类型)
 *  4B RequestId(请求的ID)
 * body 数据体，Object类型
 * Create by DiaoHao on 2020/11/27 23:10
 */
public class RpcMessageDecoder extends LengthFieldBasedFrameDecoder {
    private final Logger log = LoggerFactory.getLogger(RpcMessageDecoder.class);

    public RpcMessageDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    /**
     *
     * @param maxFrameLength 发送数据帧的最大长度
     * @param lengthFieldOffset 长度域的起始位置，即数据包中定义的 full length在包中的起始位置
     * @param lengthFieldLength full length 占了多少bit
     * @param lengthAdjustment 长度域的的拆包
     * @param initialBytesToStrip 需要跳过的字节数
     */
    public RpcMessageDecoder(int maxFrameLength,
                             int lengthFieldOffset,
                             int lengthFieldLength,
                             int lengthAdjustment,
                             int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;

        }
    }

    private Object decodeFrame(ByteBuf in) {
        checkMagic(in);;
        checkVersion(in);
        int fullLength = in.readInt();
        byte messageType = in.readByte();
        int requestId = in.readInt();
        RpcMessage rpcMessage = new RpcMessage(messageType, requestId);

        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setBody(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setBody(RpcConstants.PONG);
            return rpcMessage;
        }
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] data = new byte[bodyLength];
            in.readBytes(data);
            //解压缩
            data = new GzipCompress().decompress(data);
            //反序列化
            Serializer serializer = new KryoSerializer();
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RpcRequest request = serializer.deserialize(data, RpcRequest.class);
                rpcMessage.setBody(request);
            } else {

            }
        }
    }

    private void checkMagic(ByteBuf in) {
        int len = RpcConstants.MAGIC_CODE.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_CODE[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }

    private void checkVersion(ByteBuf in) {
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("Frame version isn't compatible" + version);
        }
    }
}


package com.dhao.remoting.transport.netty.codec;

import com.dhao.compress.Compress;
import com.dhao.compress.GzipCompress;
import com.dhao.remoting.constants.RpcConstants;
import com.dhao.remoting.dto.RpcMessage;
import com.dhao.remoting.transport.netty.server.NettyServer;
import com.dhao.serializer.KryoSerializer;
import com.dhao.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息编码器
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
public class RpcMessageEncoder extends MessageToByteEncoder<RpcMessage> {
    private final Logger log = LoggerFactory.getLogger(RpcMessageEncoder.class);

    protected void encode(ChannelHandlerContext ctx, RpcMessage rpcMessage, ByteBuf byteBuf) {
        try{
            byte messageType = rpcMessage.getMessageType();
            byte[] bodyDatas = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            //如果消息类型不是心跳检测消息，数据包的长度为 HEAD_LENGTH + BODY_LENGTH
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE &&
                    messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {

                //消息序列化
                Serializer serializer = new KryoSerializer();
                bodyDatas = serializer.serialize(rpcMessage.getBody());
                Compress compress = new GzipCompress();
                bodyDatas = compress.compress(bodyDatas);
                fullLength += bodyDatas.length;
            }
            byteBuf.writeBytes(RpcConstants.MAGIC_CODE);
            byteBuf.writeByte(RpcConstants.VERSION);
            byteBuf.writeByte(fullLength);
            byteBuf.writeByte(messageType);
            byteBuf.writeByte(rpcMessage.getRequestId());
        } catch (Exception e) {
            log.error("Encoder request error", e);
        }

    }
}

package com.dhao.remoting.constants;

import io.netty.util.CharsetUtil;
import sun.nio.cs.StandardCharsets;

import java.nio.charset.Charset;

/**
 * Create by DiaoHao on 2020/11/28 19:40
 */
public class RpcConstants {

    //魔数，用于验证消息的格式
    public static final byte[] MAGIC_CODE = {'g', 'r', 'p', 'c'};
    public static final Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;
    //RpcMessage的版本信息
    public static final byte VERSION = 1;
    //RpcMessage的消息头的长度
    public static final int TOTAL_LENGTH = 16;
    public static final int REQUEST_TYPE = 1;
    public static final int RESPONSE_TYPE = 2;

    public static final int HEAD_LENGTH = 16;
    //心跳检测的请求/相应类型
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}

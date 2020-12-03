package com.dhao.remoting.dto;

/**
 * Create by DiaoHao on 2020/11/27 23:02
 */
public class RpcMessage {
    //消息类型
    private byte messageType;
    //消息体
    private Object body;
    //请求ID
    private int requestId;

    public RpcMessage() {
    }

    public RpcMessage(byte messageType, int requestId) {
        this.messageType = messageType;
        this.requestId = requestId;
    }

    public byte getMessageType() {
        return messageType;
    }

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }


    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }
}

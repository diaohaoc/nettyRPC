package com.dhao.remoting.dto;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Create by DiaoHao on 2020/11/27 23:02
 */
public class RpcResponse<T> implements Serializable {

    private String requestId;

    //response code
    private Integer code;

    //response message
    private String message;

    //response data
    private T data;

}

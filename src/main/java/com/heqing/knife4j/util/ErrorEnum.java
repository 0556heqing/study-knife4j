package com.heqing.knife4j.util;

/**
 * 错误信息码
 * @author heqing
 * @date 2021/8/17 10:37
 */
public enum ErrorEnum {

    /**
     * 操作失败
     **/
    E_F1(-1, "操作失败"),

    /**
     * 操作成功
     **/
    E_0(0, "操作成功"),

    /**
     * 未授权
     **/
    E_401(401,"未授权"),

    /**
     * 访问受限，授权过期
     **/
    E_403(403,"访问受限，授权过期"),

    /**
     * 资源/服务未找到
     **/
    E_404(404,"资源/服务未找到"),
    ;

    private Integer code;
    private String msg;
    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}

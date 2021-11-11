package com.heqing.knife4j.util;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author heqing
 * @date 2021/8/17 11:36
 * 返回数据
 */
@ApiModel(value="ResultUtil", reference ="返回封装类")
public class ResultUtil<T> implements Serializable {

	/**
	 * 返回状态码
	 */
	@ApiModelProperty(value="返回状态码", name="code", example="0")
	private Integer code;

	/**
	 * 返回说明
	 */
	@ApiModelProperty(value="返回说明", name="msg", example="SUCCESS")
	private String msg;

	/**
	 * 返回数据
	 */
	@ApiModelProperty(value="返回数据", name="data")
	private T data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public ResultUtil(int code, String errmsg, T data) {
		this.code = code;
		this.msg = errmsg;
		this.data = data;
	}

	public static ResultUtil build(int code, String msg){
		return build(code, msg, null);
	}

	public static ResultUtil build(Integer code, String msg, Object data){
		return new ResultUtil(code, msg, data);
	}

	public static ResultUtil buildSuccess(){
		return build(ErrorEnum.E_0.getCode(), ErrorEnum.E_0.getMsg(), null);
	}

	public static ResultUtil buildSuccess(Object data){
		return build(ErrorEnum.E_0.getCode(), ErrorEnum.E_0.getMsg(), data);
	}

	public static ResultUtil buildError(){
		return build(ErrorEnum.E_F1.getCode(), ErrorEnum.E_F1.getMsg(), null);
	}

	public static ResultUtil buildError(String msg){
		return build(-1, msg, null);
	}

	public static ResultUtil buildError(Integer code, String msg){
		return build(code, msg, null);
	}

	/**
	 * 是否响应成功
	 * @return
	 */
	public boolean isSuccessful(){
		return code == 0;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}

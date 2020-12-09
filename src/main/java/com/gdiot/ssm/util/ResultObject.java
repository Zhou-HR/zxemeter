package com.gdiot.ssm.util;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResultObject implements Serializable {

	private static final long serialVersionUID = 1L;

	private String error;
	private String msg;
	private Object result;

	public ResultObject() {

	}

	public ResultObject(String error) {
		this.error = error;
	}

	public ResultObject(String error, String msg) {
		this.error = error;
		this.msg = msg;
	}
	public ResultObject(String error, String msg, Object result) {
		this.error = error;
		this.msg = msg;
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}

package com.cssrc.ibms.core.util.json;

public class JsonResult {
	private boolean success = false;
	private String message = "";
	private Object data = null;

	public JsonResult() {
	}

	public JsonResult(boolean success) {
		this.success = success;
	}

	public JsonResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public JsonResult(boolean success, Object data) {
		this.success = success;
		this.data = data;
	}

	public JsonResult(boolean success, String message, Object data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public boolean isSuccess() {
		return this.success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return this.data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}

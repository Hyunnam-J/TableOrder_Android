package com.example.tableorder;

import java.util.List;

public class Resp<T> {

	int resultCode;
	String resultMsg;
	List<T> item;

	public Resp(int resultCode, String resultMsg, List<T> item) {
		super();
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.item = item;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<T> getItem() {
		return item;
	}

	public void setItem(List<T> item) {
		this.item = item;
	}
}
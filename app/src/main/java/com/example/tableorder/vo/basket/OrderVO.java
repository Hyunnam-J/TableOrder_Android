package com.example.tableorder.vo.basket;

public class OrderVO {
	
	int tNum, tMenuSeq, aUnit, aNum, aAmt, oPrice;
	String tabNo, itemNo, pName, itemName1, itemName2, pCode, itemCode, tax, sex, area, orderNo;
	
	public int gettNum() {
		return tNum;
	}
	public void settNum(int tNum) {
		this.tNum = tNum;
	}
	public int gettMenuSeq() {
		return tMenuSeq;
	}
	public void settMenuSeq(int tMenuSeq) {
		this.tMenuSeq = tMenuSeq;
	}
	public int getaUnit() {
		return aUnit;
	}
	public void setaUnit(int aUnit) {
		this.aUnit = aUnit;
	}
	public int getaNum() {
		return aNum;
	}
	public void setaNum(int aNum) {
		this.aNum = aNum;
	}
	public int getaAmt() {
		return aAmt;
	}
	public void setaAmt(int aAmt) {
		this.aAmt = aAmt;
	}
	public int getoPrice() {
		return oPrice;
	}
	public void setoPrice(int oPrice) {
		this.oPrice = oPrice;
	}
	public String getTabNo() {
		return tabNo;
	}
	public void setTabNo(String tabNo) {
		this.tabNo = tabNo;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getItemName1() {
		return itemName1;
	}
	public void setItemName1(String itemName1) {
		this.itemName1 = itemName1;
	}
	public String getItemName2() {
		return itemName2;
	}
	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}
	public String getpCode() {
		return pCode;
	}
	public void setpCode(String pCode) {
		this.pCode = pCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}

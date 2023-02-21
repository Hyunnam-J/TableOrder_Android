package com.example.tableorder.vo.main;

public class MainItemVO {

	String comId, pName, itemName2, pos, pCode, tabNo, itemCode, uPrice, bColor;

	public MainItemVO(String comId, String pName, String itemName2, String pos, String pCode, String tabNo, String itemCode, String uPrice, String bColor) {
		this.comId = comId;
		this.pName = pName;
		this.itemName2 = itemName2;
		this.pos = pos;
		this.pCode = pCode;
		this.tabNo = tabNo;
		this.itemCode = itemCode;
		this.uPrice = uPrice;
		this.bColor = bColor;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getItemName2() {
		return itemName2;
	}

	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public String getTabNo() {
		return tabNo;
	}

	public void setTabNo(String tabNo) {
		this.tabNo = tabNo;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getuPrice() {
		return uPrice;
	}

	public void setuPrice(String uPrice) {
		this.uPrice = uPrice;
	}

	public String getbColor() {
		return bColor;
	}

	public void setbColor(String bColor) {
		this.bColor = bColor;
	}
}

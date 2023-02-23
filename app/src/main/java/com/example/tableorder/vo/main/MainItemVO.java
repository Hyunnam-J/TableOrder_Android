package com.example.tableorder.vo.main;

public class MainItemVO {

	String comId, pName, itemName1, itemName2, pos, pCode, tabNo, itemCode, bColor, tax, subUse, sex, area, stockUse;
	int uPrice, stock;
	public MainItemVO(String comId, String pName, String itemName1, String itemName2, String pos, String pCode,
					  String tabNo, String itemCode, String bColor, String tax, String subUse, String sex, String area,
					  String stockUse, int uPrice, int stock) {
		super();
		this.comId = comId;
		this.pName = pName;
		this.itemName1 = itemName1;
		this.itemName2 = itemName2;
		this.pos = pos;
		this.pCode = pCode;
		this.tabNo = tabNo;
		this.itemCode = itemCode;
		this.bColor = bColor;
		this.tax = tax;
		this.subUse = subUse;
		this.sex = sex;
		this.area = area;
		this.stockUse = stockUse;
		this.uPrice = uPrice;
		this.stock = stock;
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
	public String getbColor() {
		return bColor;
	}
	public void setbColor(String bColor) {
		this.bColor = bColor;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getSubUse() {
		return subUse;
	}
	public void setSubUse(String subUse) {
		this.subUse = subUse;
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
	public String getStockUse() {
		return stockUse;
	}
	public void setStockUse(String stockUse) {
		this.stockUse = stockUse;
	}
	public int getuPrice() {
		return uPrice;
	}
	public void setuPrice(int uPrice) {
		this.uPrice = uPrice;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

}

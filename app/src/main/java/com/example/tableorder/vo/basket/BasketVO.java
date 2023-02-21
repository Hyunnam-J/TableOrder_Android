package com.example.tableorder.vo.basket;

import com.example.tableorder.vo.main.MainItemVO;

public class BasketVO extends MainItemVO {

	int quantity;

	public BasketVO(String comId, String pName, String itemName2, String pos, String pCode, String tabNo, String itemCode, String uPrice, String bColor) {
		super(comId, pName, itemName2, pos, pCode, tabNo, itemCode, uPrice, bColor);
	}

	public BasketVO(String comId, String pName, String itemName2, String pos, String pCode, String tabNo, String itemCode, String uPrice, String bColor, int quantity) {
		super(comId, pName, itemName2, pos, pCode, tabNo, itemCode, uPrice, bColor);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}

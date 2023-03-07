package com.example.tableorder.vo.basket;

import com.example.tableorder.vo.main.MainItemVO;

public class BasketVO extends MainItemVO {

	int quantity;

	public BasketVO(String comId, String pName, String itemName1, String itemName2, String pos, String pCode,
					String tabNo, String itemCode, String bColor, String tax, String subUse, String sex, String area,
					String stockUse, int uPrice, int stock) {
		super(comId, pName, itemName1, itemName2, pos, pCode, tabNo, itemCode, bColor, tax, subUse, sex, area, stockUse, uPrice,
				stock);
	}

	public BasketVO(String comId, String pName, String itemName1, String itemName2, String pos, String pCode,
					String tabNo, String itemCode, String bColor, String tax, String subUse, String sex, String area,
					String stockUse, int uPrice, int stock, int quantity) {
		super(comId, pName, itemName1, itemName2, pos, pCode, tabNo, itemCode, bColor, tax, subUse, sex, area, stockUse,
				uPrice, stock);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}

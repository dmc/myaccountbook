package com.github.wtbyt298.myaccountbook.domain.shared.types;

/**
 * 集計区分
 */
public enum SummaryType {

	BS, //貸借対照表項目
	PL; //損益計算書項目
	
	public String label() {
		if (this.equals(BS)) return "貸借";
		return "損益";
	}
	
}

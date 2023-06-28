package com.github.wtbyt298.myaccountbook.presentation.viewmodels.summary;

import com.github.wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;

import lombok.Getter;

/**
 * 貸借対照表の各合計値を保持するクラス
 */
@Getter
public class SummaryOfBalanceSheetViewModel {

	private final int totalOfAssets;
	private final int totalOfLiabilities;
	private final int totalOfEquity;
	
	public SummaryOfBalanceSheetViewModel(int totalOfAssets, int totalOfLiabilities, int totalOfEquity) {
		this.totalOfAssets = totalOfAssets;
		this.totalOfLiabilities = totalOfLiabilities;
		this.totalOfEquity = totalOfEquity;
	}
	
	public int actualTotalOfEquity() {
		return totalOfEquity + surplus();
	}
	
	public int surplus() {
		return totalOfAssets - (totalOfLiabilities + totalOfEquity);
	}
	
	public String totalOfAssetsFormatted() {
		return AmountPresentationFormatter.yen(totalOfAssets);
	}
	
	public String totalOfLiabilitiesFormatted() {
		return AmountPresentationFormatter.yen(totalOfLiabilities);
	}
	
	public String totalOfEquityFormatted() {
		return AmountPresentationFormatter.yen(actualTotalOfEquity());
	}
	
	public String surplusFormatted() {
		return AmountPresentationFormatter.yen(surplus());
	}
	
}

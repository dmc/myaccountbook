package io.github.wtbyt298.myaccountbook.presentation.viewmodels.summary;

import io.github.wtbyt298.myaccountbook.presentation.shared.util.AmountPresentationFormatter;
import lombok.Getter;

/**
 * 月次残高の画面表示用クラス
 */
@Getter
public class MonthlyBalanceViewModel {

	private final String subAccountTitleName;
	private final int balance;
	
	public MonthlyBalanceViewModel(String subAccountTitleName, int balance) {
		this.subAccountTitleName = subAccountTitleName;
		this.balance = balance;
	}
	
	public String showBalance() {
		return AmountPresentationFormatter.yen(balance);
	}
	
}

package io.github.wtbyt298.myaccountbook.presentation.viewmodels.accounttitle;

import lombok.Getter;

/**
 * 勘定科目の表示用データを保持するクラス
 */
@Getter
public class AccountTitleViewModel {

	private final String id;
	private final String name;
	private final String accountingType;
	private final String loanType;
	private final String summaryType;
	
	public AccountTitleViewModel(String id, String name, String accountingType, String loanType, String summaryType) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
		this.loanType = loanType;
		this.summaryType = summaryType;
	}
	
}

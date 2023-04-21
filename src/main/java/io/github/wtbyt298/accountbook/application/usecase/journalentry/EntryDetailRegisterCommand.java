package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import lombok.Getter;

/**
 * 仕訳明細登録用のDTOクラス
 */
@Getter
public class EntryDetailRegisterCommand {

	private final String accountTitleId;
	private final String subAccountTitleId;
	private final String detailLoanType;
	private final int amount;
	
	public EntryDetailRegisterCommand(String accountTitleId, String subAccountTitleId, String detailLoanType, int amount) {
		this.accountTitleId = accountTitleId;
		this.subAccountTitleId = subAccountTitleId;
		this.detailLoanType = detailLoanType;
		this.amount = amount;
	}
	
}

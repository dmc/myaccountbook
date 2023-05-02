package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import lombok.Getter;

/**
 * 仕訳明細登録用のDTOクラス
 */
@Getter
public class RegisterEntryDetailCommand {

	private final String accountTitleId;    //勘定科目ID
	private final String subAccountTitleId; //補助科目ID
	private final LoanType detailLoanType;  //明細の貸借区分
	private final int amount;               //仕訳金額
	
	public RegisterEntryDetailCommand(String accountTitleId, String subAccountTitleId, String detailLoanType, int amount) {
		this.accountTitleId = accountTitleId;
		this.subAccountTitleId = subAccountTitleId;
		this.detailLoanType = LoanType.valueOf(detailLoanType);
		this.amount = amount;
	}
	
}

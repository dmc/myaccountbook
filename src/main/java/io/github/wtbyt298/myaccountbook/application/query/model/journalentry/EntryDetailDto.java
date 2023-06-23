package io.github.wtbyt298.myaccountbook.application.query.model.journalentry;

import io.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import lombok.Getter;

/**
 * DBから取得した仕訳明細データを保持するクラス
 */
@Getter
public class EntryDetailDto {
	
	private final String accountTitleId;
	private final String accountTitleName;
	private final String subAccountTitleId;
	private final String subAccountTitleName;
	private final LoanType detailLoanType;
	private final int amount;
	
	public EntryDetailDto(String accountTitleId, String accountTitleName, String subAccountTitleId, String subAccountTitleName, String detailLoanType, int amount) {
		this.accountTitleId = accountTitleId;
		this.accountTitleName = accountTitleName;
		this.subAccountTitleId = subAccountTitleId;
		this.subAccountTitleName = subAccountTitleName;
		this.detailLoanType = LoanType.valueOf(detailLoanType);
		this.amount = amount;
	}
	
	public boolean isDebit() {
		return detailLoanType.equals(LoanType.DEBIT);
	}
	
	public boolean isCredit() {
		return detailLoanType.equals(LoanType.CREDIT);
	}
	
}

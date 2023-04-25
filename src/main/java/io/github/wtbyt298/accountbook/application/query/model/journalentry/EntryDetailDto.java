package io.github.wtbyt298.accountbook.application.query.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import lombok.Getter;

/**
 * DBから取得した仕訳明細データを保持するクラス
 */
@Getter
public class EntryDetailDto {

	private final String accountTitleName;
	private final String subAccountTitleName;
	private final LoanType detailLoanType;
	private final int amount;
	
	public EntryDetailDto(String accountTitleName, String subAccountTitleName, String detailLoanType, int amount) {
		this.accountTitleName = accountTitleName;
		this.subAccountTitleName = subAccountTitleName;
		this.detailLoanType = LoanType.valueOf(detailLoanType);
		this.amount = amount;
	}
	
}

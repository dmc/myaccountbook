package io.github.wtbyt298.accountbook.domain.model.accountingelement;

import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

/**
 * 費用クラス
 */
class Expenses implements AccountingElement {

	@Override
	public String lavel() {
		return "費用";
	}

	@Override
	public LoanType loanType() {
		return LoanType.DEBIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.PL;
	}

}

package io.github.wtbyt298.accountbook.domain.model.accountingelement;

import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

/**
 * 収益クラス
 */
class Revenue implements AccountingElement {

	@Override
	public String lavel() {
		return "収益";
	}

	@Override
	public LoanType loanType() {
		return LoanType.CREDIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.PL;
	}

}

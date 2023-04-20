package io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype;

import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.SummaryType;

/**
 * 収益クラス
 */
class Revenue implements IAccountingElement {

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

package io.github.wtbyt298.myaccountbook.domain.model.accountingelement;

import io.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 負債クラス
 */
class Liabilities implements AccountingElement {

	@Override
	public String lavel() {
		return "負債";
	}

	@Override
	public LoanType loanType() {
		return LoanType.CREDIT;
	}

	@Override
	public SummaryType summaryType() {
		return SummaryType.BS;
	}

}

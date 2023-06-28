package com.github.wtbyt298.myaccountbook.domain.model.accountingelement;

import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import com.github.wtbyt298.myaccountbook.domain.shared.types.SummaryType;

/**
 * 純資産クラス
 */
class Equity implements AccountingElement {

	@Override
	public String lavel() {
		return "純資産";
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

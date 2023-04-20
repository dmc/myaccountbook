package io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype;

import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.SummaryType;

/**
 * 純資産クラス
 */
class NetAssets implements IAccountingElement {

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

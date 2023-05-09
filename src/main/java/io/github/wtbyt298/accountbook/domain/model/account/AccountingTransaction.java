package io.github.wtbyt298.accountbook.domain.model.account;

import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 会計取引クラス
 */
public class AccountingTransaction {

	final LoanType loanType;
	final Amount amount;
	
	public AccountingTransaction(LoanType loanType, Amount amount) {
		this.loanType = loanType;
		this.amount = amount;
	}
	
	boolean isDebit() {
		return loanType.equals(LoanType.DEBIT);
	}
	
	boolean isCredit() {
		return loanType.equals(LoanType.CREDIT);
	}
	
	@Override
	public String toString() {
		return loanType.toString() + "：" + amount.toString() + "円";
	}
	
}

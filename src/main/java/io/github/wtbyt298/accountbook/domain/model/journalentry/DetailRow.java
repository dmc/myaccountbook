package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.shared.Amount;
import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;
import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 明細行クラス
 */
public class DetailRow {
	
	final AccountTitleId accountTitleId;
	final SubAccountTitleId subAccountTitleId;
	final AccountingType accountingType;
	final LoanType detailLoanType;
	final Amount amount;
	
	public DetailRow(AccountTitleId accountTitleId, SubAccountTitleId subAccountTitleId, AccountingType accountingType, LoanType detailLoanType, Amount amount) {
		this.accountTitleId = accountTitleId;
		this.subAccountTitleId = subAccountTitleId;
		this.accountingType = accountingType;
		this.detailLoanType = detailLoanType;
		this.amount = amount;
	}
	
	/**
	 * @return 借方明細である場合true
	 */
	boolean isDebit() {
		return detailLoanType.equals(LoanType.DEBIT);
	}
	
	/**
	 * @return 貸方明細である場合true
	 */
	boolean isCredit() {
		return detailLoanType.equals(LoanType.CREDIT);
	}
	
	/**
	 * 
	 * @param other 組み合わせ相手の明細行
	 * @return 組み合わせ可能である場合true
	 */
	boolean canCombinate(DetailRow other) {
		return this.accountingType.canCombinate(other.accountingType);
	}
	
}

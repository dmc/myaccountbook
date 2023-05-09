package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 仕訳明細クラス
 */
public class EntryDetail {
	
	private final AccountTitleId accountTitleId;
	private final SubAccountTitleId subAccountTitleId;
	private final LoanType detailLoanType;
	final Amount amount;
	
	public EntryDetail(AccountTitleId accountTitleId, SubAccountTitleId subAccountTitleId, LoanType detailLoanType, Amount amount) {
		this.accountTitleId = accountTitleId;
		this.subAccountTitleId = subAccountTitleId;
		this.detailLoanType = detailLoanType;
		this.amount = amount;
	}
	
	/**
	 * 借方明細かどうかを判断する
	 */
	boolean isDebit() {
		return detailLoanType.equals(LoanType.DEBIT);
	}
	
	/**
	 * 貸方明細かどうかを判断する
	 */
	boolean isCredit() {
		return detailLoanType.equals(LoanType.CREDIT);
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId accountTitleId() {
		return accountTitleId;
	}
	
	/**
	 * @return 補助科目ID
	 */
	public SubAccountTitleId subAccountTitleId() {
		return subAccountTitleId;
	}
	
	/**
	 * @return 明細の貸借区分
	 */
	public LoanType detailLoanType() {
		return detailLoanType;
	}
	
	/**
	 * @return 仕訳金額
	 */
	public Amount amount() {
		return amount;
	}
	
}

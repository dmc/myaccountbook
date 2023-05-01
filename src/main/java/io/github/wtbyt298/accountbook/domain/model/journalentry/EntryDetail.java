package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 仕訳明細クラス
 */
public class EntryDetail {
	
	//TODO フィールドが多いので設計を見直したい
	private final AccountTitle accountTitle;
	private final SubAccountTitle subAccountTitle;
	private final LoanType detailLoanType;
	final Amount amount;
	
	public EntryDetail(AccountTitle accountTitle, SubAccountTitle subAccountTitle, LoanType detailLoanType, Amount amount) {
		this.accountTitle = accountTitle;
		this.subAccountTitle = subAccountTitle;
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
	boolean canCombinate(EntryDetail other) {
		return this.accountTitle.canCombinate(other.accountTitle);
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId accountTitleId() {
		return accountTitle.id();
	}
	
	/**
	 * @return 補助科目ID
	 */
	public SubAccountTitleId subAccountTitleId() {
		return subAccountTitle.id();
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
	
	//toString()などは必要になったら実装する
	
}

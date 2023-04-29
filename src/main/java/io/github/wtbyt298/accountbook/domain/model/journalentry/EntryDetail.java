package io.github.wtbyt298.accountbook.domain.model.journalentry;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
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
	 * 以下、永続化用のメソッド定義
	 * ※リポジトリクラスで内部データの取得のために呼び出す以外には使用しない
	 */
	public String accountTitleId() {
		return accountTitle.id().toString();
	}
	
	public String subAccountTitleId() {
		return subAccountTitle.id().toString();
	}
	
	public String detailLoanType() {
		return detailLoanType.toString();
	}
	
	public int amount() {
		return amount.value;
	}
	
}

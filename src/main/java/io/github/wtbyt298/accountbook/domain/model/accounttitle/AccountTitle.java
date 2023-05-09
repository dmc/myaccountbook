package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 勘定科目クラス
 * このクラスは勘定科目集約の集約ルートとして機能する
 */
public class AccountTitle {

	private final AccountTitleId id;
	private final AccountTitleName name;
	private final AccountingType accountingType;
	
	public AccountTitle(AccountTitleId id, AccountTitleName name, AccountingType accountingType) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId id() {
		return id;
	}
	
	/**
	 * @return 勘定科目名
	 */
	public AccountTitleName name() {
		return name;
	}
	
	/**
	 * @return 会計区分
	 */
	public AccountingType accountingType() {
		return accountingType;
	}
	
	/**
	 * 借方勘定科目かどうかを判断する
	 */
	public boolean isDebit() {
		return accountingType.loanType().equals(LoanType.DEBIT);
	}
	
	/**
	 * 貸方勘定科目かどうかを判断する
	 */
	public boolean isCredit() {
		return accountingType.loanType().equals(LoanType.CREDIT);
	}
	
	/**
	 * 自身を借方科目とした場合に、相手の勘定科目が貸方科目として組み合わせ可能かどうかを判断する
	 * @return 組み合わせ可能である場合true
	 */
	public boolean canCombineWith(AccountTitle other) {
		return this.accountingType.canCombineWith(other.accountingType);
	}
	
	@Override
	public String toString() {
		return id.toString() + " " + name.toString();
	}
	
}

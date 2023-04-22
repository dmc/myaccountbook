package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 勘定科目クラス
 * このクラスは勘定科目集約の集約ルートとして機能する
 */
public class AccountTitle {

	final AccountTitleId id;
	final String name;
	final AccountingType accountingType;
	
	private AccountTitle(AccountTitleId id, String name, AccountingType accountingType) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
	}
	
	public static AccountTitle reconstruct(AccountTitleId accountTitleId, String name, AccountingType accountingType) {
		return new AccountTitle(accountTitleId, name, accountingType);
	}
	
	public AccountingType accountingType() {
		return accountingType;
	}
	
	/**
	 * 自身が借方科目の場合に、他方が貸方科目として適切かどうかを判断する
	 * @param other 貸方の勘定科目
	 * @return 組み合わせ可能である場合true
	 */
	public boolean canCombinate(AccountTitle other) {
		return this.accountingType.canCombinate(other.accountingType);
	}
	
	@Override
	public String toString() {
		return id.toString() + "：" + name.toString();
	}
	
}

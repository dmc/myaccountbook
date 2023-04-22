package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 勘定科目クラス
 * このクラスは勘定科目集約の集約ルートとして機能する
 */
public class AccountTitle {

	private final AccountTitleId id;
	private final String name;
	private final AccountingType accountingType;
	
	private AccountTitle(AccountTitleId id, String name, AccountingType accountingType) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static AccountTitle reconstruct(AccountTitleId accountTitleId, String name, AccountingType accountingType) {
		return new AccountTitle(accountTitleId, name, accountingType);
	}
	
	/**
	 * 会計区分を返す
	 */
	public AccountingType accountingType() {
		return accountingType;
	}
	
	/**
	 * 補助科目をIDで検索する
	 */
	public SubAccountTitle findChildById(SubAccountTitleId subId) {
		//仮実装
		return SubAccountTitle.EMPTY;
	}
	
	@Override
	public String toString() {
		return id.toString() + "：" + name.toString();
	}
	
}

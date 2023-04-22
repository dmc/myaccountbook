package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 勘定科目クラス
 * このクラスは勘定科目集約の集約ルートとして機能する
 */
public class AccountTitle {

	private final AccountTitleId id;
	private final AccountTitleName name;
	private final AccountingType accountingType;
	
	private AccountTitle(AccountTitleId id, AccountTitleName name, AccountingType accountingType) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static AccountTitle reconstruct(AccountTitleId accountTitleId, AccountTitleName name, AccountingType accountingType) {
		return new AccountTitle(accountTitleId, name, accountingType);
	}
	
	/**
	 * @return 勘定科目IDの文字列
	 */
	public String id() {
		return id.toString();
	}
	
	/**
	 * IDで検索して補助科目を返す
	 */
	public SubAccountTitle findChild(SubAccountTitleId subId) {
		//仮実装
		return SubAccountTitle.EMPTY;
	}
	
	/**
	 * 自身を借方科目とした場合に、相手の勘定科目が貸方科目として組み合わせ可能かどうかを判断する
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

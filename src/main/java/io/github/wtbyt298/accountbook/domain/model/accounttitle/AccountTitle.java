package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import java.util.Map;
import io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType;

/**
 * 勘定科目クラス
 * このクラスは勘定科目集約の集約ルートとして機能する
 */
public class AccountTitle {

	private static final int MAX_CHILDREN_COUNT = 10; //保持できる補助科目の最大数
	private final AccountTitleId id;
	private final AccountTitleName name;
	private final AccountingType accountingType;
	private Map<SubAccountTitleId, SubAccountTitle> subAccountTitles;
	
	public AccountTitle(AccountTitleId id, AccountTitleName name, AccountingType accountingType, Map<SubAccountTitleId, SubAccountTitle> subAccountTitles) {
		this.id = id;
		this.name = name;
		this.accountingType = accountingType;
		this.subAccountTitles = subAccountTitles;
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId id() {
		return id;
	}
	
	/**
	 * IDで検索して補助科目を返す
	 * 補助科目を持っていない場合はEMPTYを返す
	 */
	public SubAccountTitle findChild(SubAccountTitleId subId) {
		if (subAccountTitles.isEmpty()) {
			return SubAccountTitle.EMPTY;
		}
		if (! containsChild(subId)) {
			throw new IllegalArgumentException("指定した補助科目は存在しません。");
		}
		return subAccountTitles.get(subId);
	}
	
	/**
	 * 補助科目を追加する
	 */
	public void addSubAccountTitle(SubAccountTitle newSubAccountTitle) {
		if (subAccountTitles.size() == MAX_CHILDREN_COUNT) {
			throw new RuntimeException("これ以上補助科目を追加できません。");
		}
		if (subAccountTitles.containsKey(newSubAccountTitle.id())) {
			throw new IllegalArgumentException("指定した補助科目は既に存在しています。");
		}
		subAccountTitles.put(newSubAccountTitle.id(), newSubAccountTitle);
	}
	
	/**
	 * 補助科目名を変更する
	 */
	public void changeSubAccountTitleName(SubAccountTitleId subId, SubAccountTitleName newName) {
		SubAccountTitle target = subAccountTitles.get(subId);
		if (! containsChild(subId)) {
			throw new IllegalArgumentException("指定した補助科目は存在しません。");
		}
		target.changeName(newName);
	}
	
	/**
	 * IDで指定した補助科目を保持しているかどうかを判断する
	 * @param subId 補助科目ID
	 */
	private boolean containsChild(SubAccountTitleId subId) {
		return subAccountTitles.containsKey(subId);
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

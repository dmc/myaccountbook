package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import java.util.Collections;
import java.util.Map;

/**
 * 補助科目のコレクションオブジェクト
 * このクラスは補助科目集約の集約ルートとして機能する
 */
public class SubAccountTitles {

	private static final int MAX_MEMBERS_COUNT = 10;
	private final Map<SubAccountTitleId, SubAccountTitle> subAccountTitles;
	
	public SubAccountTitles(Map<SubAccountTitleId, SubAccountTitle> subaccountTitles) {
		this.subAccountTitles = subaccountTitles;
	}
	
	/**
	 * 補助科目を追加する
	 */
	public void add(SubAccountTitle newMember) {
		if (subAccountTitles.size() >= MAX_MEMBERS_COUNT) {
			throw new RuntimeException("これ以上補助科目を追加できません。");
		}
		if (subAccountTitles.containsValue(newMember)) {
			throw new IllegalArgumentException("指定した補助科目は既に存在しています。");
		}
		//補助科目を持っていない場合は、はじめに「その他」という補助科目を追加する
		if (subAccountTitles.isEmpty()) {
			SubAccountTitle other = new SubAccountTitle(
				SubAccountTitleId.valueOf("0"), 
				SubAccountTitleName.valueOf("その他")
			);
			subAccountTitles.put(other.id(), other);
		}
		subAccountTitles.put(newMember.id(), newMember);
	}
	
	/**
	 * IDで検索して補助科目を取得する
	 * 補助科目を持っていない場合はEMPTYを返す
	 */
	public SubAccountTitle find(SubAccountTitleId id) {
		if (subAccountTitles.isEmpty()) {
			return SubAccountTitle.EMPTY;
		}
		if (! subAccountTitles.containsKey(id)) {
			throw new IllegalArgumentException("指定した補助科目は存在しません。");
		}
		return subAccountTitles.get(id);
	}
	
	/**
	 * 補助科目名を変更する
	 */
	public void changeSubAccountTitleName(SubAccountTitleId id, SubAccountTitleName newName) {
		if (! subAccountTitles.containsKey(id)) {
			throw new IllegalArgumentException("指定した補助科目は存在しません。");
		}
		SubAccountTitle target = subAccountTitles.get(id);
		target.rename(newName);
	}
	
	public Map<SubAccountTitleId, SubAccountTitle> elements() {
		return Collections.unmodifiableMap(subAccountTitles);
	}
	
}

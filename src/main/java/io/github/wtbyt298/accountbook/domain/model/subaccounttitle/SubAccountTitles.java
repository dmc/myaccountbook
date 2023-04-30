package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;

/**
 * 補助科目のコレクションオブジェクト
 * このクラスは補助科目集約の集約ルートとして機能する
 */
public class SubAccountTitles {

	private static final int MAX_MEMBERS_COUNT = 10;
	private final Map<SubAccountTitleId, SubAccountTitle> subAccountTitles;
	private AccountTitleId parentId; //親要素である勘定科目のID
	
	public SubAccountTitles(Map<SubAccountTitleId, SubAccountTitle> subaccountTitles, AccountTitleId parentId) {
		this.subAccountTitles = subaccountTitles;
		this.parentId = parentId;
	}
	
	/**
	 * 補助科目を追加する
	 */
	public void add(SubAccountTitleName newName) {
		if (subAccountTitles.size() >= MAX_MEMBERS_COUNT) {
			throw new RuntimeException("これ以上補助科目を追加できません。");
		}
		if (exists(newName)) {
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
		SubAccountTitleId newId = nextIdentity();
		SubAccountTitle adding = new SubAccountTitle(newId, newName);
		subAccountTitles.put(newId, adding);
	}
	
	/**
	 * 補助科目名の重複があるかどうかを判断する
	 */
	private boolean exists(SubAccountTitleName newName) {
		for (Entry<SubAccountTitleId, SubAccountTitle> each : subAccountTitles.entrySet()) {
			if (each.getValue().name().equals(newName)) return true;
		}
		return false;
	}
	
	/**
	 * 新規追加する補助科目のIDを生成する
	 */
	private SubAccountTitleId nextIdentity() {
		final int count = subAccountTitles.size();
		String nextIndex = String.valueOf(count);
		return SubAccountTitleId.valueOf(nextIndex);
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
	
	/**
	 * @return 保持している補助科目のMAP（変更不可）
	 */
	public Map<SubAccountTitleId, SubAccountTitle> elements() {
		return Collections.unmodifiableMap(subAccountTitles);
	}
	
	/**
	 * @return 勘定科目ID
	 */
	public AccountTitleId parentId() {
		return parentId;
	}
	
}

package io.github.wtbyt298.accountbook.domain.model.journalentry;

import java.util.*;

/**
 * 仕訳番号クラス
 */
public class EntryId {

	final String value;
	
	private EntryId(String value) {
		this.value = value;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static EntryId newInstance() {
		String newId = nextIdentity().toString();
		return new EntryId(newId);
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static EntryId fromString(String value) {
		return new EntryId(value);
	}

	/**
	 * ランダムなUUID文字列を生成する
	 */
	private static UUID nextIdentity() {
		return UUID.randomUUID();
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}

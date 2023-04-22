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
	 * 再構築用のファクトリメソッド
	 * @param DBから取得した伝票番号
	 */
	public static EntryId valueOf(String value) {
		return new EntryId(value);
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static EntryId newInstance() {
		final String newId = nextIdentity();
		return new EntryId(newId);
	}

	/**
	 * ランダムなUUID文字列を生成する
	 */
	private static String nextIdentity() {
		return UUID.randomUUID().toString();
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}

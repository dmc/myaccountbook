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
		String newId = nextIdentity().toString().toUpperCase();
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
	
	public String value() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "仕訳ID：" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof EntryId)) return false;
		EntryId other = (EntryId) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

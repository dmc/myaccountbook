package io.github.wtbyt298.myaccountbook.domain.model.journalentry;

import java.util.*;

import io.github.wtbyt298.myaccountbook.domain.shared.exception.DomainException;

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
	static EntryId newInstance() {
		String newId = nextIdentity().toString().toUpperCase();
		return new EntryId(newId);
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static EntryId fromString(String value) {
		if (value.isBlank()) {
			throw new DomainException("仕訳IDが空白です。");
		}
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

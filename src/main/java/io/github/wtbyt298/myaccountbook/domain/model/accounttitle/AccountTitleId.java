package io.github.wtbyt298.myaccountbook.domain.model.accounttitle;

import java.util.Objects;

import io.github.wtbyt298.myaccountbook.domain.shared.exception.DomainException;

/**
 * 勘定科目IDクラス
 */
public class AccountTitleId {

	private static final int DEFAULT_LENGTH = 3;
	final String value;
	
	private AccountTitleId(String value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static AccountTitleId valueOf(String value) {
		if (value.length() != DEFAULT_LENGTH) {
			throw new DomainException("勘定科目IDは" + DEFAULT_LENGTH + "文字で指定してください。");
		}
		return new AccountTitleId(value);
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "勘定科目ID：" + value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof AccountTitleId)) return false;
		AccountTitleId other = (AccountTitleId) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

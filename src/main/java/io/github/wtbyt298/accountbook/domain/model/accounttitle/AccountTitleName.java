package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import java.util.Objects;

/**
 * 勘定科目名クラス
 */
public class AccountTitleName {
	
	private static final int MAX_LENGTH = 32;
	final String value;
	
	private AccountTitleName(String value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static AccountTitleName valueOf(String value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("勘定科目名が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("勘定科目名は32文字以内で指定してください。");
		}
		return new AccountTitleName(value);
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "勘定科目名：" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof AccountTitleName)) return false;
		AccountTitleName other = (AccountTitleName) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

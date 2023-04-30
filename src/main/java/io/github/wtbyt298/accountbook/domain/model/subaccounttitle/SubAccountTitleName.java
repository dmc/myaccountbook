package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import java.util.Objects;

/**
 * 補助科目名クラス
 */
public class SubAccountTitleName {

	private static final int MAX_LENGTH = 32;
	final String value;
	
	private SubAccountTitleName(String value) {
		this.value = value;
	}
	
	public static SubAccountTitleName valueOf(String value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("補助科目名が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("補助科目名は32文字以内で指定してください。");
		}
		return new SubAccountTitleName(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof SubAccountTitleName)) return false;
		SubAccountTitleName other = (SubAccountTitleName) obj;
		return this.value.equals(other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

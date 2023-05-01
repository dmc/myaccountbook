package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import java.util.Objects;

/**
 * 補助科目IDクラス
 */
public class SubAccountTitleId {
	
	private static final int DEFAULT_LENGTH = 1;
	final String value;
	
	private SubAccountTitleId(String value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static SubAccountTitleId valueOf(String value) {
		if (value.length() != DEFAULT_LENGTH) {
			throw new IllegalArgumentException("補助科目IDは" + DEFAULT_LENGTH + "文字で指定してください。");
		}
		return new SubAccountTitleId(value);
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "補助科目ID：" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof SubAccountTitleId)) return false;
		SubAccountTitleId other = (SubAccountTitleId) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

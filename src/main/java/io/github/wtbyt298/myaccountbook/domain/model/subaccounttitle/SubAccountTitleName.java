package io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle;

import java.util.Objects;

import io.github.wtbyt298.myaccountbook.domain.shared.exception.DomainException;

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
			throw new DomainException("補助科目名が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new DomainException("補助科目名は32文字以内で指定してください。");
		}
		return new SubAccountTitleName(value);
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "補助科目名：" + value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof SubAccountTitleName)) return false;
		SubAccountTitleName other = (SubAccountTitleName) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

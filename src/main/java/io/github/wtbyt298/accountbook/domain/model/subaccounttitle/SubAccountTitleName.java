package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

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
	
}
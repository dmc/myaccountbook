package io.github.wtbyt298.accountbook.domain.model.accounttitle;

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
	
	@Override
	public String toString() {
		return value;
	}
	
}

package io.github.wtbyt298.accountbook.domain.model.accounttitle;

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
			throw new IllegalArgumentException("勘定科目IDは" + DEFAULT_LENGTH + "文字で指定してください。");
		}
		return new AccountTitleId(value);
	}
	
	@Override
	public String toString() {
		return this.value;
	}
	
}

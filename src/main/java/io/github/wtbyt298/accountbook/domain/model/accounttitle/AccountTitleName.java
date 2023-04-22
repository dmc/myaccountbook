package io.github.wtbyt298.accountbook.domain.model.accounttitle;

/**
 * 勘定科目名クラス
 */
public class AccountTitleName {
	
	private static final int MAX_LENGTH = 32;
	final String value;
	
	private AccountTitleName(String value) {
		this.value = value;
	}
	
	public static AccountTitleName valueOf(String value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("勘定科目名が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("勘定科目名は32文字以内で指定してください。");
		}
		return new AccountTitleName(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
	
}

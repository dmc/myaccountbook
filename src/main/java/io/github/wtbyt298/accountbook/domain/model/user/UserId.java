package io.github.wtbyt298.accountbook.domain.model.user;

/**
 * ユーザIDクラス
 */
public class UserId {
	
	private static final int MAX_LENGTH = 32;
	final String value;
	
	private UserId(String value) {
		this.value = value;
	}
	
	public static UserId valueOf(String value) {
		if (value.isBlank()) {
			throw new IllegalArgumentException("ユーザIDが空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("ユーザIDは" + MAX_LENGTH + "文字以内で指定してください。");
		}
		return new UserId(value);
	}

	@Override
	public String toString() {
		return value;
	}
	
}

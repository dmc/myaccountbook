package io.github.wtbyt298.accountbook.domain.model.journalentry;

/**
 * 摘要クラス
 */
public class Description {

	private static final int MAX_LENGTH = 64;
	
	final String value;
	
	private Description(String value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static Description valueOf(String value) {
		if (value.isEmpty()) {
			throw new IllegalArgumentException("摘要が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new IllegalArgumentException("摘要は" + MAX_LENGTH + "文字以内で入力してください。");
		}
		return new Description(value);
	}
 
	@Override
	public String toString() {
		return value;
	}
	
}

package wtbyt298.myaccountbook.domain.model.journalentry;

import java.util.Objects;

import wtbyt298.myaccountbook.domain.shared.exception.DomainException;

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
		if (value.isBlank()) {
			throw new DomainException("摘要が空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new DomainException("摘要は" + MAX_LENGTH + "文字以内で入力してください。");
		}
		return new Description(value);
	}
 
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return "摘要：" + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof Description)) return false;
		Description other = (Description) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

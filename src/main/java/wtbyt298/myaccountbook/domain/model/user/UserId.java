package wtbyt298.myaccountbook.domain.model.user;

import java.util.Objects;

import wtbyt298.myaccountbook.domain.shared.exception.DomainException;

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
			throw new DomainException("ユーザIDが空白です。");
		}
		if (value.length() > MAX_LENGTH) {
			throw new DomainException("ユーザIDは" + MAX_LENGTH + "文字以内で指定してください。");
		}
		return new UserId(value);
	}
	
	public String value() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof UserId)) return false;
		UserId other = (UserId) obj;
		return Objects.equals(this.value, other.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
	
}

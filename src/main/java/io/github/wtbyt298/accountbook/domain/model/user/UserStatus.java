package io.github.wtbyt298.accountbook.domain.model.user;

/**
 * ユーザステータス
 */
public enum UserStatus {

	ACTIVE,   //有効なユーザ
	INACTIVE; //無効なユーザ（退会済み）
	
	@Override
	public String toString() {
		if (this == UserStatus.ACTIVE) return "ACTIVE";
		return "INACTIVE";
	}
	
}

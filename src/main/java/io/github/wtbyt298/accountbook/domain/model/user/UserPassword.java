package io.github.wtbyt298.accountbook.domain.model.user;

import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードクラス
 */
public class UserPassword {

	final String value;
	
	private UserPassword(String value) {
		this.value = value;
	}
	
	/**
	 * ファクトリメソッド
	 */
	public static UserPassword valueOf(String value) {
		if (! isValidFormat(value)) {
			throw new IllegalArgumentException("パスワードの形式が正しくありません。");
		}
		return new UserPassword(value);
	}
	
	/**
	 * パスワードをエンコードした文字列を返す
	 */
	String encode() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(value);
	}
	
	/**
	 * パスワードのフォーマットが適切かどうかを判断する
	 * フォーマットは英大文字と英小文字と数字を少なくとも1文字以上含み、8文字以上16文字以内とする
	 */
	private static boolean isValidFormat(String value) {
		return Pattern.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[!-~]{8,16}", value);
	}
	
}

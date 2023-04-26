package io.github.wtbyt298.accountbook.domain.model.user;

import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * パスワードクラス
 */
public class EncodedUserPassword {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	final String value;
	
	private EncodedUserPassword(String value) {
		this.value = value;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static EncodedUserPassword fromRawPassword(String rawPassword) {
		if (! isValidFormat(rawPassword)) {
			throw new IllegalArgumentException("パスワードの形式が正しくありません。");
		}
		String encoded = generateHash(rawPassword);
		return new EncodedUserPassword(encoded);
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static EncodedUserPassword valueOf(String value) {
		return new EncodedUserPassword(value);
	}
	
	/**
	 * パスワードを比較し一致するかどうかを判断する
	 */
	boolean match(String rawPassword) {
		return encoder.matches(rawPassword, value);
	}
	
	/**
	 * 生のパスワードをハッシュ化した文字列を返す
	 */
	private static String generateHash(String rawPassword) {
		return encoder.encode(rawPassword);
	}
	
	/**
	 * パスワードのフォーマットが適切かどうかを判断する
	 * フォーマットは英大文字と英小文字と数字を少なくとも1文字以上含み、8文字以上16文字以内とする
	 */
	private static boolean isValidFormat(String value) {
		return Pattern.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[!-~]{8,16}", value);
	}
	
}

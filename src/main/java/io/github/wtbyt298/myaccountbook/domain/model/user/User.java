package io.github.wtbyt298.myaccountbook.domain.model.user;

/**
 * ユーザクラス
 */
public class User {

	private final UserId userId;                           //ユーザID
	private final EncodedUserPassword encodedUserPassword; //ハッシュ化したパスワード
	private final String mailAddress;                      //メールアドレス　※必要があれば値オブジェクトとして設計し直す
	private UserStatus userStatus;                         //ACTIVE：利用中　INACTIVE：退会済み
	
	private User(UserId userId, EncodedUserPassword encodedUserPassword, String mailAddress, UserStatus userStatus) {
		this.userId = userId;
		this.encodedUserPassword = encodedUserPassword;
		this.mailAddress = mailAddress;
		this.userStatus = userStatus;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static User create(UserId userId, EncodedUserPassword password, String mailAddress) {
		return new User(userId, password, mailAddress, UserStatus.ACTIVE); //新規作成時は有効なユーザとして作成する
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static User reconstruct(UserId userId, EncodedUserPassword password, String mailAddress, UserStatus userStatus) {
		return new User(userId, password, mailAddress, userStatus);
	}
	
	/**
	 * 与えられたパスワードが保持しているパスワードに合致するかどうかを判断する
	 */
	public boolean acceptPassword(String rawPassword) {
		return encodedUserPassword.match(rawPassword);
	}
	
	/**
	 * 無効なユーザに変更する
	 */
	public void disable() {
		if (userStatus.equals(UserStatus.INACTIVE)) {
			throw new RuntimeException("ユーザが既に退会しています。");
		}
		userStatus = UserStatus.INACTIVE;
	}
	
	/**
	 * ユーザが有効かどうかを判断する
	 */
	public boolean isActive() {
		return userStatus.equals(UserStatus.ACTIVE);
	}
	
	/**
	 * @return ユーザID
	 */
	public UserId id() {
		return userId;
	}
	
	/**
	 * @return ハッシュ化したパスワード
	 */
	public EncodedUserPassword password() {
		return encodedUserPassword;
	}
	
	/**
	 * @return メールアドレス
	 */
	public String mailAddress() {
		return mailAddress;
	}
	
	/**
	 * @return ユーザステータス
	 */
	public UserStatus userStatus() {
		return userStatus;
	}
	
	@Override
	public String toString() {
		return "ユーザID：" + userId.toString() + " ステータス：" + userStatus.toString();
	}
	
}

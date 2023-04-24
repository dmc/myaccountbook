package io.github.wtbyt298.accountbook.domain.model.user;

/**
 * ユーザクラス
 */
public class User {

	private final UserId userId;             //ユーザID
	private final UserPassword userPassword; //パスワード
	private final String mailAddress;        //メールアドレス　※必要があれば値オブジェクトとして設計し直す
	private UserStatus userStatus;           //ACTIVE：利用中　INACTIVE：退会済み
	
	private User(UserId userId, UserPassword userPassword, String mailAddress, UserStatus userStatus) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.mailAddress = mailAddress;
		this.userStatus = userStatus;
	}
	
	/**
	 * 新規作成用のファクトリメソッド
	 */
	public static User create(UserId userId, UserPassword password, String mailAddress) {
		return new User(userId, password, mailAddress, UserStatus.ACTIVE); //新規作成時は有効なユーザとして作成する
	}
	
	/**
	 * 再構築用のファクトリメソッド
	 */
	public static User reconstruct(UserId userId, UserPassword password, String mailAddress, UserStatus userStatus) {
		return new User(userId, password, mailAddress, userStatus);
	}
	
	//無効なユーザに変更する処理
	public void disable() {
		if (userStatus.equals(UserStatus.INACTIVE)) {
			throw new RuntimeException("ユーザが既に退会しています。");
		}
		userStatus = UserStatus.INACTIVE;
	}
	
	/**
	 * @return ユーザID
	 */
	public String id() {
		return userId.value;
	}
	
	/**
	 * @return ハッシュ化したパスワード
	 */
	public String encodedPassword() {
		return userPassword.encode();
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
	public String userStatus() {
		return userStatus.toString();
	}
	
	@Override
	public String toString() {
		return userId.toString();
	}
	
}

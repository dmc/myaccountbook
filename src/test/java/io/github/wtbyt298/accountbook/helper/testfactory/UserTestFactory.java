package io.github.wtbyt298.accountbook.helper.testfactory;

import io.github.wtbyt298.accountbook.domain.model.user.EncodedUserPassword;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * テスト用のユーザインスタンスを生成するクラス
 */
public class UserTestFactory {

	/**
	 * ユーザID：TEST_ID
	 * パスワード（ハッシュ化前）：Test0123OK
	 * メールアドレス：test@example.com
	 * @return 新規ユーザ
	 */
	public static User create() {
		return User.create(
			UserId.valueOf("TEST_ID"), 
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
			"test@example.com"
		);
	}
	
}

package io.github.wtbyt298.accountbook.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void 新規作成時は有効なユーザとして作成される() {
		//when
		User user = User.create(
			UserId.valueOf("TEST_USER"), 
			UserPassword.valueOf("Test0123OK"), 
			"test@example.com"
		);
		
		//then
		assertEquals("TEST_USER", user.id());
		//パスワードのハッシュ化についてはUserPasswordクラスでテストしている
		assertEquals("test@example.com", user.mailAddress());
		assertEquals(UserStatus.ACTIVE.toString(), user.userStatus());
	}
	
	@Test
	void 再構築メソッドに値を渡すと渡した値でインスタンスが生成される() {
		//when
		User user = User.reconstruct(
			UserId.valueOf("TEST_USER"), 
			UserPassword.valueOf("Test0123OK"), 
			"test@example.com",
			UserStatus.ACTIVE
		);
		
		//then
		assertEquals("TEST_USER", user.id());
		assertEquals("test@example.com", user.mailAddress());
		assertEquals(UserStatus.ACTIVE.toString(), user.userStatus());
	}
	
	@Test
	void ユーザステータスを有効から無効に変更できる() {
		//given 
		User user = User.create(
			UserId.valueOf("TEST_USER"), 
			UserPassword.valueOf("Test0123OK"), 
			"test@example.com"
		);
		
		//when
		user.disable();
		
		//then
		assertEquals(UserStatus.INACTIVE.toString(), user.userStatus());
	}

}

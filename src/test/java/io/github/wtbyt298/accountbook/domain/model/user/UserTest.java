package io.github.wtbyt298.accountbook.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	void 新規作成時は有効なユーザとして作成される() {
		//when
		User user = User.create(
			UserId.valueOf("TEST_USER"), 
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
			"test@example.com"
		);
		
		//then
		assertEquals("TEST_USER", user.id());
		//パスワードのハッシュ化についてはEncodedUserPasswordクラスでテストしている
		assertEquals("test@example.com", user.mailAddress());
		assertEquals(UserStatus.ACTIVE.toString(), user.userStatus());
	}
	
	@Test
	void 再構築メソッドに値を渡すと渡した値でインスタンスが生成される() {
		//when
		User user = User.reconstruct(
			UserId.valueOf("TEST_USER"), 
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
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
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
			"test@example.com"
		);
		
		//when
		user.disable();
		
		//then
		assertEquals(UserStatus.INACTIVE.toString(), user.userStatus());
	}
	
	@Test
	void 既に無効化している場合にdisableメソッドを呼ぶと例外発生() {
		//given ユーザステータスが既に無効になっている
		User user = User.create(
			UserId.valueOf("TEST_USER"), 
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
			"test@example.com"
		);
		user.disable();
		
		//when
		Exception exception = assertThrows(RuntimeException.class, () -> user.disable());
		
		//then
		assertEquals("ユーザが既に退会しています。", exception.getMessage());
	}

}

package io.github.wtbyt298.myaccountbook.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.myaccountbook.helper.testfactory.UserTestFactory;

class UserTest {

	@Test
	void 新規作成時は有効なユーザとして作成される() {
		//when:
		User user = User.create(
			UserId.valueOf("TEST_USER"), 
			EncodedUserPassword.fromRawPassword("Test0123OK"), 
			"test@example.com"
		);
		
		//then:
		assertEquals("TEST_USER", user.id().value);
		assertTrue(user.acceptPassword("Test0123OK")); //生のパスワードのエンコード結果が保持しているパスワードハッシュと一致する
		assertEquals("test@example.com", user.mailAddress());
		assertEquals(UserStatus.ACTIVE, user.userStatus());
	}
	
	@Test
	void 再構築メソッドに値を渡すと渡した値でインスタンスが生成される() {
		//when:
		User user = User.reconstruct(
			UserId.valueOf("TEST_USER"), 
			EncodedUserPassword.fromHashedPassword("DBから取得したパスワードハッシュ"), 
			"test@example.com",
			UserStatus.ACTIVE
		);
		
		//then:
		assertEquals("TEST_USER", user.id().value);
		assertEquals("DBから取得したパスワードハッシュ", user.password().value);
		assertEquals("test@example.com", user.mailAddress());
		assertEquals(UserStatus.ACTIVE, user.userStatus());
	}
	
	@Test
	void ユーザステータスを有効から無効に変更できる() {
		//given:新規作成されたユーザ
		//この時点ではユーザステータスは有効になっている
		User user = UserTestFactory.create();
		assertEquals(UserStatus.ACTIVE, user.userStatus());
		assertTrue(user.isActive());
		
		//when:ユーザを無効化する
		user.disable();
		
		//then:ユーザステータスが無効になっている
		assertEquals(UserStatus.INACTIVE, user.userStatus());
		assertFalse(user.isActive());
	}
	
	@Test
	void 既に無効化している場合にdisableメソッドを呼ぶと例外発生() {
		//given:ユーザステータスが既に無効になっている
		User user = UserTestFactory.create();
		user.disable();
		
		//when:再度disableメソッドを呼び出す
		Exception exception = assertThrows(RuntimeException.class, () -> user.disable());
		
		//then:想定した例外が発生している
		assertEquals("ユーザが既に退会しています。", exception.getMessage());
	}

}

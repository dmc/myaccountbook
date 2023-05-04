package io.github.wtbyt298.accountbook.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EncodedUserPasswordTest {
	
	@Test
	void 制約を満たした文字列で初期化できる() {
		//given:パスワードのフォーマットについて
		//英大文字、英小文字、数字がそれぞれ1文字以上含まれること
		//8文字以上16文字以下であること
		String rawPassword = "Test0123OK";
		
		//when:
		//then:
		assertDoesNotThrow(() -> EncodedUserPassword.fromRawPassword(rawPassword));
	}
	
	@Test
	void 英字のみで初期化すると例外発生() {
		//given:英字のみパスワード　※長さの制約は満たしている
		String rawPassword = "AAAAaaaa";
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> EncodedUserPassword.fromRawPassword(rawPassword));
		
		//then:
		assertEquals("パスワードの形式が正しくありません。", exception.getMessage());
	}
	
	@Test
	void 数字のみで初期化すると例外発生() {
		//given:数字のみパスワード　※長さの制約は満たしている
		String rawPassword = "12345678";
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> EncodedUserPassword.fromRawPassword(rawPassword));
		
		//then:
		assertEquals("パスワードの形式が正しくありません。", exception.getMessage());
	}
	
	@Test
	void 長さ8文字より小さい文字列で初期化すると例外発生() {
		//given:長さ7のパスワード　※使用文字の制約は満たしている
		String rawPassword = "Test123";
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> EncodedUserPassword.fromRawPassword(rawPassword));
		
		//then:
		assertEquals("パスワードの形式が正しくありません。", exception.getMessage());
	}
	
	@Test
	void 長さ16文字より大きい文字列で初期化すると例外発生() {
		//given:長さ17のパスワード　※使用文字の制約は満たしている
		String rawPassword = "Test012345678OVER";
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> EncodedUserPassword.fromRawPassword(rawPassword));
		
		//then:
		assertEquals("パスワードの形式が正しくありません。", exception.getMessage());
	}
	
	@Test
	void ハッシュ化前後のパスワードを照合すると合致する() {
		//given:パスワードのフォーマットはOK
		String rawPassword = "Test0123OK";
		
		//when:
		EncodedUserPassword encodedUserPassword = EncodedUserPassword.fromRawPassword(rawPassword);
		
		//then:
		assertTrue(encodedUserPassword.match(rawPassword));
	}

}

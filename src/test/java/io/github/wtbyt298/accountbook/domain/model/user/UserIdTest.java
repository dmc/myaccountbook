package io.github.wtbyt298.accountbook.domain.model.user;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class UserIdTest {

	@Test
	void 長さ32以内の文字列で初期化できる() {
		//given:"A"を32回連結した文字列
		String str = new StringWriter() {{for (int i = 0; i < 32; i++) write("A");}}.toString();
		
		//when:
		UserId id = UserId.valueOf(str);
		
		//then:
		assertEquals(str, id.value);
	}
	
	@Test
	void 長さ32より大きい文字列で初期化すると例外発生() {
		//given:"A"を33回連結した文字列
		String str = new StringWriter() {{for (int i = 0; i < 33; i++) write("A");}}.toString();
		
		//when:
		Exception exception = assertThrows(IllegalArgumentException.class, () -> UserId.valueOf(str));

		//then:
		assertEquals("ユーザIDは32文字以内で指定してください。", exception.getMessage());
	}
	
	@Test
	void 空白で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(IllegalArgumentException.class, () -> UserId.valueOf(" "));
		
		//then:
		assertEquals("ユーザIDが空白です。", exception.getMessage());
	}

	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		UserId a = UserId.valueOf("USER_ID_1");
		UserId b = UserId.valueOf("USER_ID_1");
		UserId c = UserId.valueOf("USER_ID_10");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

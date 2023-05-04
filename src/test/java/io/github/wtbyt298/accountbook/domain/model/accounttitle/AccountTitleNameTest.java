package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class AccountTitleNameTest {

	@Test
	void 長さ32以内の文字列で初期化できる() {
		//given:"A"を32回連結した文字列
		String within = new StringWriter() {{for (int i = 0; i < 32; i++) write("A");}}.toString();
		
		//when:
		AccountTitleName name = AccountTitleName.valueOf(within);
		
		//then:
		assertEquals(within, name.value);
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> AccountTitleName.valueOf(""));
		
		//then:
		assertEquals("勘定科目名が空白です。", exception.getMessage());
	}
	
	@Test
	void 長さ32より大きい文字列で初期化すると例外発生() {
		//given:"A"を33回連結した文字列
		String over = new StringWriter() {{for (int i = 0; i < 33; i++) write("A");}}.toString();
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> AccountTitleName.valueOf(over));
		
		//then:
		assertEquals("勘定科目名は32文字以内で指定してください。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		AccountTitleName a = AccountTitleName.valueOf("現金");
		AccountTitleName b = AccountTitleName.valueOf("現金");
		AccountTitleName c = AccountTitleName.valueOf("普通預金");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

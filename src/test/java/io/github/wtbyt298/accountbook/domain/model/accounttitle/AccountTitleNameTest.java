package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class AccountTitleNameTest {

	@Test
	void 長さ32以内の文字列で初期化できる() {
		String within = new StringWriter() {{for (int i = 0; i < 32; i++) write("A");}}.toString(); //"A"を32回連結した文字列
		assertDoesNotThrow(() -> AccountTitleName.valueOf(within));
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		assertThrows(IllegalArgumentException.class, () -> AccountTitleName.valueOf(""));
	}
	
	@Test
	void 長さ32より大きい文字列で初期化すると例外発生() {
		String over = new StringWriter() {{for (int i = 0; i < 33; i++) write("A");}}.toString(); //"A"を33回連結した文字列
		assertThrows(IllegalArgumentException.class, () -> AccountTitleName.valueOf(over));
	}
}

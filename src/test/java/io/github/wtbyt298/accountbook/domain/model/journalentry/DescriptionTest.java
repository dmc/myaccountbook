package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class DescriptionTest {

	@Test
	void 長さ64以内の文字列で初期化できる() {
		String within = new StringWriter() {{for (int i = 0; i < 64; i++) write("A");}}.toString(); //"A"を64回連結した文字列
		assertDoesNotThrow(() -> Description.valueOf(within));
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		assertThrows(IllegalArgumentException.class, () -> Description.valueOf(""));
	}
	
	@Test
	void 長さ64より大きい文字列で初期化すると例外発生() {
		String over = new StringWriter() {{for (int i = 0; i < 65; i++) write("A");}}.toString(); //"A"を65回連結した文字列
		assertThrows(IllegalArgumentException.class, () -> Description.valueOf(over));
	}
	
}

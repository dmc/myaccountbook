package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DescriptionTest {

	@Test
	void x64文字以内の文字列で初期化できる() {
		String withinMaxLength = generateTestString(64);
		Description description = Description.valueOf(withinMaxLength);
		assertEquals(withinMaxLength, description.toString());
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		assertThrows(IllegalArgumentException.class, () -> Description.valueOf(""));
	}
	
	@Test
	void x64文字より長い文字列で初期化すると例外発生() {
		String over = generateTestString(65);
		assertThrows(IllegalArgumentException.class, () -> Description.valueOf(over));
	}
	
	/**
	 * 任意の長さの文字列を生成するヘルパーメソッド
	 * 指定した回数だけ"A"を連結した文字列を返す
	 */
	private String generateTestString(int length) {
		String str = "";
		for (int i = 0; i < length; i++) {
			str += "A";
		}
		return str;
	}

}

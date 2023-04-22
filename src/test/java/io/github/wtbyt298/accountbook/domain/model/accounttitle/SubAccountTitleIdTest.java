package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubAccountTitleIdTest {

	@Test
	void 長さ1の文字列で初期化できる() {
		assertDoesNotThrow(() -> SubAccountTitleId.valueOf("1"));
	}
	
	@Test
	void 長さ1の文字列以外で初期化すると例外発生() {
		assertThrows(IllegalArgumentException.class, () -> SubAccountTitleId.valueOf("")); //長さ0の場合
		assertThrows(IllegalArgumentException.class, () -> SubAccountTitleId.valueOf("11")); //長さ2の場合
	}

}

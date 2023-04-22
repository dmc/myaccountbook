package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTitleIdTest {

	@Test
	void 長さ3の文字列で初期化できる() {
		assertDoesNotThrow(() -> AccountTitleId.valueOf("101"));
	}
	
	@Test
	void 長さ3以外の文字列で初期化すると例外発生() {
		assertThrows(IllegalArgumentException.class, () -> AccountTitleId.valueOf("10")); //長さ2の場合
		assertThrows(IllegalArgumentException.class, () -> AccountTitleId.valueOf("1011")); //長さ4の場合
	}

}

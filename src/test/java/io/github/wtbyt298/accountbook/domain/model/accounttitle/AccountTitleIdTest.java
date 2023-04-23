package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTitleIdTest {

	@Test
	void 長さ3の文字列で初期化できる() {
		//when
		AccountTitleId id = AccountTitleId.valueOf("101");
		
		//then
		assertEquals("101", id.value);
	}
	
	@Test
	void 長さ3より小さい文字列で初期化すると例外発生() {
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> AccountTitleId.valueOf("10"));
		
		//then
		assertEquals("勘定科目IDは3文字で指定してください。", exception.getMessage());
	}
	
	@Test
	void 長さ3より大きい文字列で初期化すると例外発生() {
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> AccountTitleId.valueOf("1011"));
		
		//then
		assertEquals("勘定科目IDは3文字で指定してください。", exception.getMessage());
	}

}

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

	@Test
	void 保持している文字列が同一なら等価判定() {
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id2 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id3 = SubAccountTitleId.valueOf("1");
		assertEquals(id1, id2);
		assertNotEquals(id1, id3);
	}
	
	@Test
	void 等価と判定されるならハッシュ値も等しくなる() {
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id2 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id3 = SubAccountTitleId.valueOf("1");
		assertEquals(id1.hashCode(), id2.hashCode());
		assertNotEquals(id1.hashCode(), id3.hashCode());
	}
	
}

package io.github.wtbyt298.accountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubAccountTitleIdTest {

	@Test
	void 長さ1の文字列で初期化できる() {
		//when
		SubAccountTitleId id = SubAccountTitleId.valueOf("1");
		
		//then
		assertEquals("1", id.value);
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> SubAccountTitleId.valueOf(""));
		
		//then
		assertEquals("補助科目IDは1文字で指定してください。", exception.getMessage());
	}

	@Test
	void 長さ1より大きい文字列で初期化すると例外発生() {
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> SubAccountTitleId.valueOf("11"));
		
		//then
		assertEquals("補助科目IDは1文字で指定してください。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定() {
		//when
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id2 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id3 = SubAccountTitleId.valueOf("1");
		
		//then
		assertEquals(id1, id2);
		assertNotEquals(id2, id3);
	}
	
	@Test
	void 等価と判定されるならハッシュ値も等しくなる() {
		//when
		SubAccountTitleId id1 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id2 = SubAccountTitleId.valueOf("0");
		SubAccountTitleId id3 = SubAccountTitleId.valueOf("1");
		
		//then
		assertEquals(id1.hashCode(), id2.hashCode());
		assertNotEquals(id2.hashCode(), id3.hashCode());
	}
	
}

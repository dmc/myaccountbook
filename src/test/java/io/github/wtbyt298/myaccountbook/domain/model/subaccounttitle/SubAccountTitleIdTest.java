package io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SubAccountTitleIdTest {

	@Test
	void 長さ1の文字列で初期化できる() {
		//when:
		SubAccountTitleId id = SubAccountTitleId.valueOf("1");
		
		//then:
		assertEquals("1", id.value);
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> SubAccountTitleId.valueOf(""));
		
		//then:
		assertEquals("補助科目IDは1文字で指定してください。", exception.getMessage());
	}

	@Test
	void 長さ1より大きい文字列で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> SubAccountTitleId.valueOf("11"));
		
		//then:
		assertEquals("補助科目IDは1文字で指定してください。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		SubAccountTitleId a = SubAccountTitleId.valueOf("0");
		SubAccountTitleId b = SubAccountTitleId.valueOf("0");
		SubAccountTitleId c = SubAccountTitleId.valueOf("1");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

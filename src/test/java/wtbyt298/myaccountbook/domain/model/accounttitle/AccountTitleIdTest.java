package wtbyt298.myaccountbook.domain.model.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;

class AccountTitleIdTest {

	@Test
	void 長さ3の文字列で初期化できる() {
		//when:
		AccountTitleId id = AccountTitleId.valueOf("101");
		
		//then:
		assertEquals("101", id.value);
	}
	
	@Test
	void 長さ3より小さい文字列で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> AccountTitleId.valueOf("10"));
		
		//then:
		assertEquals("勘定科目IDは3文字で指定してください。", exception.getMessage());
	}
	
	@Test
	void 長さ3より大きい文字列で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> AccountTitleId.valueOf("1011"));
		
		//then:
		assertEquals("勘定科目IDは3文字で指定してください。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		AccountTitleId a = AccountTitleId.valueOf("101");
		AccountTitleId b = AccountTitleId.valueOf("101");
		AccountTitleId c = AccountTitleId.valueOf("201");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}

}

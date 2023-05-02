package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EntryIdTest {
	
	@Test
	void 任意の文字列で初期化できる() {
		//when:
		EntryId id = EntryId.fromString("abcdefgh");
		
		//then:
		assertEquals("abcdefgh", id.value);
	}
	
	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		EntryId a = EntryId.fromString("ABC");
		EntryId b = EntryId.fromString("ABC");
		EntryId c = EntryId.fromString("0123456789");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}

}

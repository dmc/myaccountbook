package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntryIdTest {
	
	@Test
	void 任意の文字列で初期化できる() {
		//when
		EntryId id = EntryId.fromString("abcdefgh");
		
		//then
		assertEquals("abcdefgh", id.value);
	}

}

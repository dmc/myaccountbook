package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EntryIdTest {

	@Test
	void 新規作成用のファクトリメソッド() {
		EntryId id = EntryId.newInstance();
		System.out.println(id);
	}
	
	@Test
	void 任意の文字列からインスタンスを生成できる() {
		EntryId id = EntryId.valueOf("ABCDE12345");
		assertEquals("ABCDE12345", id.toString());
	}

}

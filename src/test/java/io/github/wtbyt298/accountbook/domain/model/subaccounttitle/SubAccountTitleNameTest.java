package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class SubAccountTitleNameTest {

	@Test
	void 長さ32以内の文字列で初期化できる() {
		//given "A"を32回連結した文字列
		String within = new StringWriter() {{for (int i = 0; i < 32; i++) write("A");}}.toString();
		
		//when
		SubAccountTitleName name = SubAccountTitleName.valueOf(within);
		
		//then
		assertEquals(within, name.value);
	}
	
	@Test
	void 空文字列で初期化すると例外発生() {
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> SubAccountTitleName.valueOf(""));
		
		//then
		assertEquals("補助科目名が空白です。", exception.getMessage());
	}
	
	@Test
	void 長さ32より大きい文字列で初期化すると例外発生() {
		//given "A"を33回連結した文字列
		String over = new StringWriter() {{for (int i = 0; i < 33; i++) write("A");}}.toString();
		
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> SubAccountTitleName.valueOf(over));
		
		//then
		assertEquals("補助科目名は32文字以内で指定してください。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定() {
		//when
		SubAccountTitleName name1 = SubAccountTitleName.valueOf("食料品");
		SubAccountTitleName name2 = SubAccountTitleName.valueOf("食料品");
		SubAccountTitleName name3 = SubAccountTitleName.valueOf("外食");
		
		//then
		assertEquals(name1, name2);
		assertNotEquals(name2, name3);
	}
	
	@Test
	void 等価と判定されるならハッシュ値も等しくなる() {
		//when
		SubAccountTitleName name1 = SubAccountTitleName.valueOf("食料品");
		SubAccountTitleName name2 = SubAccountTitleName.valueOf("食料品");
		SubAccountTitleName name3 = SubAccountTitleName.valueOf("外食");
		
		//then
		assertEquals(name1.hashCode(), name2.hashCode());
		assertNotEquals(name2.hashCode(), name3.hashCode());
	}

}

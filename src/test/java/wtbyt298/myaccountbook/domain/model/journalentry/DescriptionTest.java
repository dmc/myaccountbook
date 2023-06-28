package wtbyt298.myaccountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.journalentry.Description;

class DescriptionTest {

	@Test
	void 長さ64以内の文字列で初期化できる() {
		//given:"A"を64回連結した文字列
		String within = new StringWriter() {{for (int i = 0; i < 64; i++) write("A");}}.toString();
		
		//when:
		Description description = Description.valueOf(within);
		
		//then:
		assertEquals(within, description.value);
	}
	
	@Test
	void 長さ64より大きい文字列で初期化すると例外発生() {
		//given:"A"を65回連結した文字列
		String over = new StringWriter() {{for (int i = 0; i < 65; i++) write("A");}}.toString();
		
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> Description.valueOf(over));
		
		//then:
		assertEquals("摘要は64文字以内で入力してください。", exception.getMessage());
	}
	
	@Test
	void 空白で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> Description.valueOf(" "));
		
		//then:
		assertEquals("摘要が空白です。", exception.getMessage());
	}
	
	@Test
	void 保持している文字列が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		Description a = Description.valueOf("食料品買い物");
		Description b = Description.valueOf("食料品買い物");
		Description c = Description.valueOf("奨学金返済");
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

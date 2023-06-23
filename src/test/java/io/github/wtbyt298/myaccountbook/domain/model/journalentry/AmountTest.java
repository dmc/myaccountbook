package io.github.wtbyt298.myaccountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AmountTest {

	@Test
	void 正の数を引数に渡すと正しいインスタンスが生成される() {
		//when:
		Amount five = Amount.valueOf(5);
		
		//then:
		assertEquals(5, five.value);
	}
	
	@Test
	void ゼロを引数に渡しても正しいインスタンスが生成される() {
		//when:
		Amount zero = Amount.valueOf(0);
		
		//then:
		assertEquals(0, zero.value);
	}
	
	@Test
	void 負の数を引数に渡すと例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> Amount.valueOf(-1));
		
		//then:
		assertEquals("金額には0以上の数値を指定してください。", exception.getMessage());
	}
	
	@Test
	void 金額と金額を加算して新しい金額を返す() {		
		//given:
		Amount five = Amount.valueOf(5);
		Amount ten = Amount.valueOf(10);
		
		//when:
		Amount result = five.plus(ten);
		
		//then:
		assertEquals(Amount.valueOf(15), result);
	}
	@Test
	void 整数aの値がb以上なら減算可能() {
		//given:
		Amount ten = Amount.valueOf(10);
		Amount five = Amount.valueOf(5);
		
		//when:
		Amount result = ten.minus(five);
		
		//then:
		assertEquals(Amount.valueOf(5), result);
	}
	
	@Test
	void 整数aの値がbより小さいときは減算できない() {
		//given:
		Amount five = Amount.valueOf(5); 
		Amount ten = Amount.valueOf(10);
		  
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> five.minus(ten));
		
		//then:
		assertEquals("結果が負の値となるため計算できません。", exception.getMessage());
	}
	
	@Test
	void 保持している数値が等しければ等価判定されハッシュ値も等しくなる() {
		//when:
		Amount a = Amount.valueOf(10);
		Amount b = Amount.valueOf(10);
		Amount c = Amount.valueOf(100);
		
		//then
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

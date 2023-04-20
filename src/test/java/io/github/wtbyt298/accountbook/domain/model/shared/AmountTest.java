package io.github.wtbyt298.accountbook.domain.model.shared;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AmountTest {

	@Test
	void 正の数を引数に渡すと正しいインスタンスが生成される() {
		Amount five = Amount.valueOf(5);
		assertEquals(5, five.value());
	}
	
	@Test
	void ゼロを引数に渡しても正しいインスタンスが生成される() {
		Amount zero = Amount.valueOf(0);
		assertEquals(0, zero.value());
	}
	
	@Test
	void 負の数を引数に渡すと例外発生() {
		assertThrows(IllegalArgumentException.class, () -> Amount.valueOf(-1));
	}
	
	@Test
	void 金額と金額を加算して新しい金額を返す() {
		Amount five = Amount.valueOf(5);
		Amount ten = Amount.valueOf(10);
		assertEquals(Amount.valueOf(15), five.plus(ten));
	}

	@Test
	void 整数aの値がb以上なら減算可能() {
		Amount ten = Amount.valueOf(10);
		Amount five = Amount.valueOf(5);
		assertEquals(Amount.valueOf(5), ten.minus(five));
	}
	
	@Test
	void 整数aの値がbより小さいときは減算できない() {
		Amount five = Amount.valueOf(5);
		Amount ten = Amount.valueOf(10);
		assertThrows(IllegalArgumentException.class, () -> five.minus(ten));
	}
	
	@Test
	void 金額が等しければハッシュ値も等しくなる() {
		Amount a = Amount.valueOf(10);
		Amount b = Amount.valueOf(10);
		assertEquals(a.hashCode(), b.hashCode());
	}
	
}

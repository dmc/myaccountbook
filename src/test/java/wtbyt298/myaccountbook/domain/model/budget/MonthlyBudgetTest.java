package wtbyt298.myaccountbook.domain.model.budget;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.budget.MonthlyBudget;

class MonthlyBudgetTest {

	@Test
	void 任意の整数型で初期化できる() {
		//when:
		MonthlyBudget budget = MonthlyBudget.valueOf(10000);
		
		//then:
		assertEquals(10000, budget.value());
	}
	
	@Test
	void 負の数で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> MonthlyBudget.valueOf(-1));
		
		//then:
		assertEquals("予算は0以上で設定してください。", exception.getMessage());
	}
	
	@Test
	void 大きさが1000000より大きい整数で初期化すると例外発生() {
		//when:
		Exception exception = assertThrows(RuntimeException.class, () -> MonthlyBudget.valueOf(1_000_001));
		
		//then:
		assertEquals("予算は1000000以下で設定してください。", exception.getMessage());
	}
	
	@Test
	void 予算が0でなければ予算に占める実績値を予算比として計算できる() {
		//given:予算額は10000
		MonthlyBudget budget = MonthlyBudget.valueOf(10000);
		
		//when:実績値8500として予算比を計算
		BigDecimal result = budget.ratioOfBudget(8500);
		
		//then:予算比は0.8500として計算される
		assertEquals(0.8500, result.doubleValue());
	}
	
	@Test
	void 予算が0の場合予算比の計算結果は0となる() {
		//given:予算額は0
		MonthlyBudget budget = MonthlyBudget.valueOf(0);
		
		//when:実績値10000として予算比を計算
		BigDecimal result = budget.ratioOfBudget(10000);
		
		//then:予算比は0として計算される
		assertEquals(BigDecimal.ZERO, result);
	}
	
	@Test
	void 保持している数値が等しければ等価と判定される() {
		//when:
		MonthlyBudget a = MonthlyBudget.valueOf(10000);
		MonthlyBudget b = MonthlyBudget.valueOf(10000);
		MonthlyBudget c = MonthlyBudget.valueOf(10000);
		
		//then:
		assertEquals(a, b);
		assertEquals(b, c);
		assertEquals(a, c);
	}
	
}

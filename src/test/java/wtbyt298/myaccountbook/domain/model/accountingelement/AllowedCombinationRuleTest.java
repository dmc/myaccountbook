package wtbyt298.myaccountbook.domain.model.accountingelement;

import static org.junit.jupiter.api.Assertions.*;
import static wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType.*;

import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.accountingelement.AllowedCombinationRule;

class AllowedCombinationRuleTest {

	@Test
	void 資産は資産と負債と純資産と収益と組み合わせ可能() {
		assertAll(
			() -> assertTrue(AllowedCombinationRule.ok(ASSETS, ASSETS)),
			() -> assertTrue(AllowedCombinationRule.ok(ASSETS, LIABILITIES)),
			() -> assertTrue(AllowedCombinationRule.ok(ASSETS, EQUITY)),
			() -> assertTrue(AllowedCombinationRule.ok(ASSETS, REVENUE)),
			//資産：費用　は不可
			() -> assertFalse(AllowedCombinationRule.ok(ASSETS, EXPENSES))
		);
	}

	@Test
	void 負債は資産と負債と収益と組み合わせ可能() {
		assertAll(
			() -> assertTrue(AllowedCombinationRule.ok(LIABILITIES, ASSETS)),
			() -> assertTrue(AllowedCombinationRule.ok(LIABILITIES, LIABILITIES)),
			() -> assertTrue(AllowedCombinationRule.ok(LIABILITIES, REVENUE)),
			//負債：純資産　は不可
			() -> assertFalse(AllowedCombinationRule.ok(LIABILITIES, EQUITY)),
			//負債：費用　は不可
			() -> assertFalse(AllowedCombinationRule.ok(LIABILITIES, EXPENSES))
		);
	}
	
	@Test
	void 純資産は資産と負債と純資産と費用と組み合わせ可能() {
		assertAll(
			() -> assertTrue(AllowedCombinationRule.ok(EQUITY, ASSETS)),
			() -> assertTrue(AllowedCombinationRule.ok(EQUITY, EQUITY)),
			() -> assertTrue(AllowedCombinationRule.ok(EQUITY, LIABILITIES)),
			() -> assertTrue(AllowedCombinationRule.ok(EQUITY, EXPENSES)),
			//純資産：収益　は不可
			() -> assertFalse(AllowedCombinationRule.ok(EQUITY, REVENUE))
		);
	}
	
	@Test
	void 費用は資産と負債と純資産と収益と組み合わせ可能() {
		assertAll(
			() -> assertTrue(AllowedCombinationRule.ok(EXPENSES, ASSETS)),
			() -> assertTrue(AllowedCombinationRule.ok(EXPENSES, LIABILITIES)),
			() -> assertTrue(AllowedCombinationRule.ok(EXPENSES, EQUITY)),
			() -> assertTrue(AllowedCombinationRule.ok(EXPENSES, REVENUE)),
			//費用：費用　は不可
			() -> assertFalse(AllowedCombinationRule.ok(EXPENSES, EXPENSES))
		);
	}
	
	@Test
	void 収益は借方に指定することはできない() {
		assertAll(
			() -> assertFalse(AllowedCombinationRule.ok(REVENUE, ASSETS)),
			() -> assertFalse(AllowedCombinationRule.ok(REVENUE, LIABILITIES)),
			() -> assertFalse(AllowedCombinationRule.ok(REVENUE, EQUITY)),
			() -> assertFalse(AllowedCombinationRule.ok(REVENUE, EXPENSES)),
			() -> assertFalse(AllowedCombinationRule.ok(REVENUE, REVENUE))
		);
	}
	
}

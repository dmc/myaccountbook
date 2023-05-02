package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testfactory.EntryDetailTestFactory;

class EntryDetailsTest {
	
	@Test
	void 借方仕訳明細のみのリストで初期化すると例外発生() {
		//given:
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(LoanType.DEBIT));
		
		//when:
		Exception exception = assertThrows(IllegalArgumentException.class, () -> new EntryDetails(list));
	
		//then:
		assertEquals("貸借それぞれに少なくとも1件ずつの明細が必要です。", exception.getMessage());
	}
	
	@Test
	void 貸方仕訳明細のみのリストで初期化すると例外発生() {
		//given:
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(LoanType.DEBIT));
		
		//when:
		Exception exception = assertThrows(IllegalArgumentException.class, () -> new EntryDetails(list));
		
		//then:
		assertEquals("貸借それぞれに少なくとも1件ずつの明細が必要です。", exception.getMessage());
	}
	
	@Test
	void 貸借それぞれに少なくとも1件ずつの仕訳明細が存在していれば初期化できる() {
		//given:
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(LoanType.DEBIT));
		list.add(EntryDetailTestFactory.create(LoanType.CREDIT));
		
		//when:
		EntryDetails entryDetails = new EntryDetails(list);
		
		//then:
		assertEquals(2, entryDetails.elements.size());
	}
	
	@Test
	void 保持している仕訳明細の合計金額を計算できる() {
		//given:貸借それぞれに2件ずつの仕訳明細が存在している
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		list.add(EntryDetailTestFactory.create(LoanType.DEBIT, 200));
		list.add(EntryDetailTestFactory.create(LoanType.CREDIT, 100));
		list.add(EntryDetailTestFactory.create(LoanType.CREDIT, 200));
		
		//when:
		EntryDetails entryDetails = new EntryDetails(list);
		
		//then:借方合計、貸方合計が正しく計算できる
		assertEquals(Amount.valueOf(300), entryDetails.debitSum());
		assertEquals(Amount.valueOf(300), entryDetails.creditSum());
	}
	
	@Test
	void 貸借合計が一致しているかどうかを判断できる() {
		//given:貸借合計が一致しているものと一致していないものを用意
		List<EntryDetail> list1 = new ArrayList<>();
		list1.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		list1.add(EntryDetailTestFactory.create(LoanType.CREDIT, 100));
		
		List<EntryDetail> list2 = new ArrayList<>();
		list2.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		list2.add(EntryDetailTestFactory.create(LoanType.CREDIT, 200));
		
		//when
		EntryDetails correct = new EntryDetails(list1);
		EntryDetails incorrect = new EntryDetails(list2);
		
		//then
		assertTrue(correct.isSameTotal());
		assertFalse(incorrect.isSameTotal());
	}
	
	@Test
	void 明細行の貸借組み合わせの可否を判断できる() {
		//given:
		//借方：資産　貸方：負債、収益
		//借方：費用　貸方：負債、収益
		//今回の例だと全てtrueになるはずである
		List<EntryDetail> list = new ArrayList<>();
		list.add(EntryDetailTestFactory.create(AccountingType.ASSETS, LoanType.DEBIT, 100));
		list.add(EntryDetailTestFactory.create(AccountingType.EXPENSES, LoanType.DEBIT, 100));
		list.add(EntryDetailTestFactory.create(AccountingType.LIABILITIES, LoanType.CREDIT, 100));
		list.add(EntryDetailTestFactory.create(AccountingType.REVENUE, LoanType.CREDIT, 100));
		
		//when:
		EntryDetails entryDetails = new EntryDetails(list);
		
		//then:
		assertTrue(entryDetails.isCorrectCombination());
	}

}

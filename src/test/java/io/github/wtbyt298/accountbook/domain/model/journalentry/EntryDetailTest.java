package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.github.wtbyt298.accountbook.domain.model.shared.Amount;
import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;

class EntryDetailTest {

	//TODO テストコードを改良する
	private static List<DetailRow> details = new ArrayList<>();
	
	@BeforeAll
	static void prepare() {
		//会計区分と貸借区分と金額以外はここでは重要でないのでnullを渡している
		DetailRow debit1 = new DetailRow(null, null, LoanType.DEBIT, Amount.valueOf(100));
		DetailRow debit2 = new DetailRow(null, null, LoanType.DEBIT, Amount.valueOf(200));
		DetailRow debit3 = new DetailRow(null, null, LoanType.DEBIT, Amount.valueOf(300));
		DetailRow credit1 = new DetailRow(null, null, LoanType.CREDIT, Amount.valueOf(100));
		DetailRow credit2 = new DetailRow(null, null, LoanType.CREDIT, Amount.valueOf(500));
		details.add(debit1);
		details.add(debit2);
		details.add(debit3);
		details.add(credit1);
		details.add(credit2);
	}
	
	@Test
	void 借方明細のみのリストで初期化すると例外発生() {
		List<DetailRow> onlyDebit = details.stream().filter(each -> each.isDebit()).collect(Collectors.toList());
		assertThrows(IllegalArgumentException.class, () -> new EntryDetail(onlyDebit));
	}
	
	@Test
	void 貸方明細のみのリストで初期化すると例外発生() {
		List<DetailRow> onlyCredit = details.stream().filter(each -> each.isCredit()).collect(Collectors.toList());
		assertThrows(IllegalArgumentException.class, () -> new EntryDetail(onlyCredit));
	}
	
	@Test
	void 貸借それぞれに少なくとも1件ずつの明細行が存在していれば初期化できる() {
		assertDoesNotThrow(() -> new EntryDetail(details));
	}
	
	@Test
	void 合計金額の計算() {
		EntryDetail detail = new EntryDetail(details);
		assertEquals(Amount.valueOf(600), detail.debitSum());
		assertEquals(Amount.valueOf(600), detail.creditSum());
		assertTrue(detail.isSameTotal());
	}
	
//	@Test
//	void 明細行の貸借可能組み合わせの確認() {
//		//借方：資産　貸方：負債、収益
//		//借方：費用　貸方：負債、収益
//		//今回の例だと全てtrueになるはずである
//		EntryDetail detail = new EntryDetail(details);
//		assertTrue(detail.isCollectCombination());
//	}

}

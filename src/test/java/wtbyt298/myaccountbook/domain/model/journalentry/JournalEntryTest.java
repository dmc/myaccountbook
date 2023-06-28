package wtbyt298.myaccountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.journalentry.Amount;
import wtbyt298.myaccountbook.domain.model.journalentry.DealDate;
import wtbyt298.myaccountbook.domain.model.journalentry.Description;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetails;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.helper.testfactory.EntryDetailTestFactory;

class JournalEntryTest {

	@Test
	void 新規生成メソッドに取引日等を渡すと渡した値でインスタンスが新規生成される() {
		//given:生成に必要なドメインオブジェクトは正しく生成されている
		DealDate dealDate = DealDate.valueOf(LocalDate.now());
		Description description = Description.valueOf("テスト用の仕訳です。");
		
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 100));
		EntryDetails entryDetails = new EntryDetails(elements);

		//when:新規生成メソッドを呼び出す
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳です。"), 
			entryDetails
		);
		
		//then:渡した値でインスタンスが生成されている
		assertEquals(dealDate, journalEntry.dealDate());
		assertEquals(description, journalEntry.description());
		assertEquals(2, journalEntry.entryDetails().size());
	}
	
	@Test
	void 仕訳明細の貸借の合計が一致していない場合は例外発生() {
		//given:借方明細と貸方明細の金額が異なっている
		DealDate dealDate = DealDate.valueOf(LocalDate.now());
		Description description = Description.valueOf("テスト用の仕訳です。");
		
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 9999));
		EntryDetails entryDetails = new EntryDetails(elements);

		//when:新規生成メソッドを呼び出す
		Exception exception = assertThrows(RuntimeException.class, () -> 
			JournalEntry.create(dealDate, description, entryDetails)
		);
		
		//then:想定した例外が発生している
		assertEquals("明細の貸借合計が一致していません。", exception.getMessage());
	}
	
	@Test
	void 再構築メソッドに仕訳ID等を渡すと渡した値でインスタンスが再構築される() {
		//given:生成に必要なドメインオブジェクトは正しく生成されている
		EntryId entryId = EntryId.fromString("DBから取得したID");
		DealDate dealDate = DealDate.valueOf(LocalDate.now());
		Description description = Description.valueOf("テスト用の仕訳です。");
		
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 100));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 100));
		EntryDetails entryDetails = new EntryDetails(elements);
		
		//when:再構築メソッドを呼び出す
		JournalEntry journalEntry = JournalEntry.reconstruct(entryId, dealDate, description, entryDetails);
		
		//then:渡した値でインスタンスが生成されている
		assertEquals(entryId, journalEntry.id());
		assertEquals(dealDate, journalEntry.dealDate());
		assertEquals(description, journalEntry.description());
		assertEquals(2, journalEntry.entryDetails().size());
	}
	
	@Test
	void 仕訳合計金額を正しく計算できる() {
		//given:複数の仕訳明細を持つ
		//借方：1000円　2000円　計3000円
		//貸方：1500円　1500円　計3000円
		//この場合仕訳合計金額は3000円となる
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 1000));
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 2000));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 1500));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 1500));
		EntryDetails entryDetails = new EntryDetails(elements);

		//when:仕訳のインスタンスを生成する
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳です。"), 
			entryDetails
		);
		
		//then:合計金額が正しく計算されている
		assertEquals(Amount.valueOf(3000), journalEntry.totalAmount());
	}
	
	@Test
	void 貸借を逆にした仕訳に変換できる() {
		//given:借方2件、貸方1件の仕訳明細を持った仕訳インスタンス
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 1000));
		elements.add(EntryDetailTestFactory.create(LoanType.DEBIT, 1000));
		elements.add(EntryDetailTestFactory.create(LoanType.CREDIT, 2000));
		EntryDetails entryDetails = new EntryDetails(elements);
		
		JournalEntry journalEntry = JournalEntry.reconstruct(
			EntryId.fromString("DBから取得したID"),
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳です。"), 
			entryDetails
		);
		
		//when:仕訳明細の貸借を入れ替える
		JournalEntry transposed = journalEntry.toReversingJournalEntry();
		
		//then:それぞれの仕訳明細の貸借区分が変更されている
		assertEquals(LoanType.CREDIT, transposed.entryDetails().get(0).detailLoanType());
		assertEquals(LoanType.CREDIT, transposed.entryDetails().get(1).detailLoanType());
		assertEquals(LoanType.DEBIT, transposed.entryDetails().get(2).detailLoanType());
	}

}

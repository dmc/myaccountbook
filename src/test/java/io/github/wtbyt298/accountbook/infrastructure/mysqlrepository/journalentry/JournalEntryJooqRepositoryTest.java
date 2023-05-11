package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.journalentry.DealDate;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Description;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetails;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class JournalEntryJooqRepositoryTest {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;

	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void リポジトリに保存した仕訳と再構築した仕訳の各属性が一致する() {
		//given:ユーザが既に作成されている
		User user = userTestDataCreator.create();
		JournalEntry created = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳"), 
			testEntryDetails()
		);
		
		//when:仕訳を保存し、その後IDで検索して取得する
		journalEntryRepository.save(created, user.id());
		JournalEntry found = journalEntryRepository.findById(created.id());
		
		//then:DBから取得したレコードと保存した仕訳の各項目が一致する
		//仕訳のアサーションを行う
		assertEquals(found.id(), created.id());                           //仕訳ID
		assertEquals(found.dealDate(), created.dealDate());               //取引日
		assertEquals(found.description(), created.description()); //摘要
		assertEquals(found.fiscalYearMonth(), created.fiscalYearMonth()); //会計年月
		assertEquals(found.totalAmount(), created.totalAmount()); //仕訳合計金額
		//子要素である仕訳明細のアサーションを行う
		for (int i = 0; i < found.entryDetails().size(); i++) {
			EntryDetail createdElement = created.entryDetails().get(i);
			EntryDetail foundElement = found.entryDetails().get(i);
			assertEquals(foundElement.accountTitleId(), createdElement.accountTitleId());       //勘定科目ID
			assertEquals(foundElement.subAccountTitleId(), createdElement.subAccountTitleId()); //補助科目ID
			assertEquals(foundElement.detailLoanType(), createdElement.detailLoanType());       //明細貸借区分
			assertEquals(foundElement.amount(), createdElement.amount());                       //金額
		}
	}

	@Test
	void 仕訳IDを指定してdropメソッドを呼び出すと該当する仕訳が削除される() {
		//given:ユーザが既に作成されている
		User user = userTestDataCreator.create();
		//仕訳を作成して保存する
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳"), 
			testEntryDetails()
		);
		journalEntryRepository.save(journalEntry, user.id());
		//この時点ではレコードはDBに存在している
		assertTrue(journalEntryRepository.exists(journalEntry.id()));
		
		//when:仕訳を削除する
		journalEntryRepository.drop(journalEntry.id());
		
		//then:DBから該当するレコードが削除されている
		assertFalse(journalEntryRepository.exists(journalEntry.id()));
	}
	
	/**
	 * テスト用の仕訳明細のコレクションオブジェクトを生成する
	 * 借方：401 食費 費用 1000円 / 402 消耗品 費用 2000円
	 * 貸方：101 現金 資産 3000円
	 */
	private EntryDetails testEntryDetails() {
		List<EntryDetail> elements = new ArrayList<>();
		elements.add(testEntryDetail("401", "食費", AccountingType.EXPENSES, LoanType.DEBIT, 1000));
//		elements.add(testEntryDetail("402", "消耗品", AccountingType.EXPENSES, LoanType.DEBIT, 2000));
		elements.add(testEntryDetail("101", "現金", AccountingType.ASSETS, LoanType.CREDIT, 1000));
		return new EntryDetails(elements);
	}
	
	/**
	 * テスト用の仕訳明細インスタンスを生成する
	 */
	private EntryDetail testEntryDetail(String accountTitleId, String accountTitleName, AccountingType accountingType ,LoanType loanType, int amount) {
		return new EntryDetail(
			accountTitleTestDataCreator.create(accountTitleId, accountTitleName, accountingType).id(), 
			SubAccountTitleId.valueOf("0"), 
			loanType, 
			Amount.valueOf(amount)
		);
	}

}

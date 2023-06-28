package com.github.wtbyt298.myaccountbook.infrastructure.mysqlrepository.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import com.github.wtbyt298.myaccountbook.domain.model.user.User;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import com.github.wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;
import com.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

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
		//given:ユーザと勘定科目が既に作成されている
		User user = userTestDataCreator.create();
		
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		accountTitleTestDataCreator.create("402", "消耗品費", AccountingType.EXPENSES);
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		
		//仕訳を作成する
		JournalEntry created = new JournalEntryTestFactory.Builder()
			.addDetail("401", "0", LoanType.DEBIT, 1000)
			.addDetail("402", "0", LoanType.DEBIT, 2000)
			.addDetail("101", "0", LoanType.CREDIT, 3000)
			.build();
		
		//when:仕訳を保存し、その後IDで検索して取得する
		journalEntryRepository.save(created, user.id());
		JournalEntry found = journalEntryRepository.findById(created.id());
		
		//then:DBから取得したレコードと保存した仕訳の各項目が一致する
		//仕訳のアサーションを行う
		assertAll(
			() -> assertEquals(found.id(), created.id()),                           //仕訳ID
			() -> assertEquals(found.dealDate(), created.dealDate()),               //取引日
			() -> assertEquals(found.description(), created.description()),         //摘要
			() -> assertEquals(found.fiscalYearMonth(), created.fiscalYearMonth()), //会計年月
			() -> assertEquals(found.totalAmount(), created.totalAmount())          //仕訳合計金額
		);
		
		//子要素である仕訳明細のアサーションを行う
		for (int i = 0; i < found.entryDetails().size(); i++) {
			EntryDetail createdElement = created.entryDetails().get(i);
			EntryDetail foundElement = found.entryDetails().get(i);
			assertAll(
				() -> assertEquals(foundElement.accountTitleId(), createdElement.accountTitleId()),       //勘定科目ID
				() -> assertEquals(foundElement.subAccountTitleId(), createdElement.subAccountTitleId()), //補助科目ID
				() -> assertEquals(foundElement.detailLoanType(), createdElement.detailLoanType()),       //明細貸借区分
				() -> assertEquals(foundElement.amount(), createdElement.amount())                        //金額
			);
		}
	}

	@Test
	void 仕訳IDを指定してdropメソッドを呼び出すと該当する仕訳が削除される() {
		//given:ユーザと勘定科目が既に作成されている
		User user = userTestDataCreator.create();
		
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		
		//仕訳を作成して保存する
		JournalEntry entry = new JournalEntryTestFactory.Builder().build();
		journalEntryRepository.save(entry, user.id());
		
		//この時点ではレコードはDBに存在している
		assertTrue(journalEntryRepository.exists(entry.id()));
		
		//when:仕訳を削除する
		journalEntryRepository.drop(entry.id());
		
		//then:DBから該当するレコードが削除されている
		assertFalse(journalEntryRepository.exists(entry.id()));
	}
	
	@Test
	void 存在しない仕訳を取得しようとすると例外発生() {
		//given:仕訳は作成されていない
		
		//when:存在しない仕訳IDを指定してfindByIdメソッドを呼び出す
		EntryId entryId = EntryId.fromString("TEST_ID");
		Exception exception = assertThrows(RuntimeException.class, () -> journalEntryRepository.findById(entryId));
		
		//then:想定した例外が発生している
		assertEquals("指定した仕訳は存在しません。", exception.getMessage());
	}

}

package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static generated.tables.JournalEntries.*;
import static generated.tables.EntryDetails.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
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
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
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
	
	@Autowired
	private DSLContext jooq;
	
	@Test
	void リポジトリに保存した仕訳とDBから取得した値が一致する() {
		//given:ユーザが既に作成されている
		User user = userTestDataCreator.create();
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳"), 
			testEntryDetails()
		);
		
		//when:仕訳を保存する
		journalEntryRepository.save(journalEntry, user.id());
		
		//then:DBから取得したレコードと保存した仕訳の各項目が一致する
		//仕訳のアサーションを行う
		Record record = jooq.select()
							.from(JOURNAL_ENTRIES)
							.where(JOURNAL_ENTRIES.ENTRY_ID.eq(journalEntry.id().value()))
							.fetchOne();
		assertEquals(journalEntry.id().value(), record.get(JOURNAL_ENTRIES.ENTRY_ID));                   //仕訳ID
		assertEquals(journalEntry.dealDate().value(), record.get(JOURNAL_ENTRIES.DEAL_DATE));            //取引日
		assertEquals(journalEntry.description().value(), record.get(JOURNAL_ENTRIES.ENTRY_DESCRIPTION)); //摘要
		assertEquals(journalEntry.fiscalYearMonth(), record.get(JOURNAL_ENTRIES.FISCAL_YEARMONTH));      //会計年月
		assertEquals(journalEntry.totalAmount().value(), record.get(JOURNAL_ENTRIES.TOTAL_AMOUNT));      //仕訳合計金額
		assertEquals(user.id().value(), record.get(JOURNAL_ENTRIES.USER_ID));                            //ユーザID
		//子要素である仕訳明細のアサーションを行う
		Result<Record> result = jooq.select()
									.from(ENTRY_DETAILS)
									.where(ENTRY_DETAILS.ENTRY_ID.eq(journalEntry.id().value()))
									.fetch();
		for (int i = 0; i < result.size(); i++) {
			EntryDetail row = journalEntry.entryDetails().get(i); //現在選択されている仕訳明細
			assertEquals(row.accountTitleId().value(), result.get(i).get(ENTRY_DETAILS.ACCOUNTTITLE_ID));        //勘定科目ID
			assertEquals(row.subAccountTitleId().value(), result.get(i).get(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID)); //補助科目ID
			assertEquals(row.detailLoanType().toString(), result.get(i).get(ENTRY_DETAILS.LOAN_TYPE));           //明細貸借区分
			assertEquals(row.amount().value(), result.get(i).get(ENTRY_DETAILS.AMOUNT));                         //金額
		}
	}
	
	@Test
	void 仕訳IDを指定してdropメソッドを呼び出すと該当する仕訳が削除される() {
		//given:ユーザが既に作成されている
		User user = userTestDataCreator.create();
		JournalEntry journalEntry = JournalEntry.create(
			DealDate.valueOf(LocalDate.now()), 
			Description.valueOf("テスト用の仕訳"), 
			testEntryDetails()
		);
		//仕訳を保存する
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
		List<EntryDetail> list = new ArrayList<>();
		list.add(testEntryDetail("401", "食費", AccountingType.EXPENSES, LoanType.DEBIT, 1000));
		list.add(testEntryDetail("402", "消耗品", AccountingType.EXPENSES, LoanType.DEBIT, 2000));
		list.add(testEntryDetail("101", "現金", AccountingType.ASSETS, LoanType.CREDIT, 3000));
		return new EntryDetails(list);
	}
	
	/**
	 * テスト用の仕訳明細インスタンスを生成する
	 */
	private EntryDetail testEntryDetail(String accountTitleId, String accountTitleName, AccountingType accountingType ,LoanType loanType, int amount) {
		return new EntryDetail(
			accountTitleTestDataCreator.create(accountTitleId, accountTitleName, accountingType), 
			new SubAccountTitle(SubAccountTitleId.valueOf("0"), SubAccountTitleName.valueOf("その他")), 
			loanType, 
			Amount.valueOf(amount)
		);
	}

}

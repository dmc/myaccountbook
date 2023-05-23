package io.github.wtbyt298.accountbook.infrastructure.mysqlquery.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.EntryDetailDto;
import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.JournalEntryOrderKey;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.user.User;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testdatacreator.UserTestDataCreator;
import io.github.wtbyt298.accountbook.helper.testfactory.JournalEntryTestFactory;

@SpringBootTest
@Transactional
class FetchJournalEntryDataJooqQueryServiceTest {

	@Autowired
	private FetchJournalEntryDataQueryService fetchJournalEntryQueryService;
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@BeforeEach
	void setUp() {
		//テストに必要な勘定科目を作成する
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		accountTitleTestDataCreator.create("401", "食費", AccountingType.EXPENSES);
	}
	
	@Test
	void IDを指定して該当する仕訳データを取得できる() {
		//given:ユーザ、仕訳が作成されている
		User user =userTestDataCreator.create();
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("401", "0", LoanType.DEBIT, 1000)
			.addDetail("101", "0", LoanType.CREDIT, 1000)
			.build();
		journalEntryRepository.save(entry, user.id());
		
		//when:IDを指定して該当する仕訳データを取得する
		JournalEntryDto dto = fetchJournalEntryQueryService.fetchOne(entry.id());
		
		//then:DTOと仕訳の対応する属性が一致している
		assertEquals(entry.id().value(), dto.getEntryId());
		assertEquals(entry.dealDate().value(), dto.getDealDate());
		assertEquals(entry.description().value(), dto.getDescription());
		assertEquals(entry.totalAmount().value(), dto.getTotalAmount());
		//仕訳明細DTOのアサーションを行う
		EntryDetailDto debitDto = dto.getEntryDetails().get(0);
		assertEquals("食費", debitDto.getAccountTitleName());
		assertEquals("", debitDto.getSubAccountTitleName());
		assertEquals(LoanType.DEBIT, debitDto.getDetailLoanType());
		assertEquals(1000, debitDto.getAmount());
		EntryDetailDto creditDto = dto.getEntryDetails().get(1);
		assertEquals("現金", creditDto.getAccountTitleName());
		assertEquals("", creditDto.getSubAccountTitleName());
		assertEquals(LoanType.CREDIT, creditDto.getDetailLoanType());
		assertEquals(1000, creditDto.getAmount());
	}
	
	@Test
	void IDに該当する仕訳が存在しない場合は例外発生() {
		//given:仕訳は作成されていない
		
		//when:仕訳IDを指定してテスト対象メソッドを実行する
		EntryId entryId = EntryId.fromString("TEST");
		Exception exception = assertThrows(RuntimeException.class, () -> fetchJournalEntryQueryService.fetchOne(entryId));
		
		//then:想定した例外が発生している
		assertEquals("該当するデータが見つかりませんでした。", exception.getMessage());
	}
	
	@Test
	void 年月を指定して仕訳データの一覧を取得できる() {
		//given:ユーザが作成されている
		//仕訳が複数作成されている（単月ではなく、複数の月に渡るもの）
		User user = userTestDataCreator.create();
		JournalEntry entry1 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 1))
			.build();
		JournalEntry entry2 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 2))
			.build();
		JournalEntry entry3 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 5, 1))
			.build();
		journalEntryRepository.save(entry1, user.id());
		journalEntryRepository.save(entry2, user.id());
		journalEntryRepository.save(entry3, user.id());
		
		//when:年月を指定して仕訳データの一覧を取得する
		YearMonth current = YearMonth.of(2023, 4);
		List<JournalEntryDto> data = fetchJournalEntryQueryService.fetchAll(current, JournalEntryOrderKey.DEAL_DATE, user.id());
		
		//then:取得した仕訳データの取引日の年月が、指定した年月と一致する
		for (JournalEntryDto dto : data) {
			assertEquals(current.getYear(), dto.getDealDate().getYear());
			assertEquals(current.getMonth(), dto.getDealDate().getMonth());
		}
	}
	
	@Test
	void 指定した年月に該当する仕訳が存在しない場合は例外発生() {
		//given:仕訳は作成されていない
		
		//when:年月などを指定してテスト対象メソッドを実行する
		Exception exception = assertThrows(RuntimeException.class, () -> 
			fetchJournalEntryQueryService.fetchAll(YearMonth.now(), JournalEntryOrderKey.DEAL_DATE, UserId.valueOf("TEST_USER"))
		);
		
		//then:想定した例外が発生している
		assertEquals("該当するデータが見つかりませんでした。", exception.getMessage());
	}
	
	@Test
	void 仕訳を取引日順の昇順で並べ替えることができる() {
		//given:ユーザと仕訳が作成されている
		User user = userTestDataCreator.create();
		JournalEntry entry1 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 30))
			.build();
		JournalEntry entry2 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 10))
			.build();
		JournalEntry entry3 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 20))
			.build();
		journalEntryRepository.save(entry1, user.id());
		journalEntryRepository.save(entry2, user.id());
		journalEntryRepository.save(entry3, user.id());
		
		//when:取引日をソートのキーに指定して仕訳データの一覧を取得する
		List<JournalEntryDto> data = fetchJournalEntryQueryService.fetchAll(YearMonth.of(2023, 4), JournalEntryOrderKey.DEAL_DATE, user.id());
		
		//then:取得結果が日付の昇順で並べ替えられている
		//この場合、entry2 -> entry3 -> entry1 の順になっているはずである
		assertEquals(entry2.dealDate().value(), data.get(0).getDealDate());
		assertEquals(entry3.dealDate().value(), data.get(1).getDealDate());
		assertEquals(entry1.dealDate().value(), data.get(2).getDealDate());
	}
	
	@Test
	void 仕訳を合計金額の降順で並べ替えることができる() {
		//given:ユーザと仕訳が作成されている
		User user = userTestDataCreator.create();
		JournalEntry entry1 = new JournalEntryTestFactory.Builder()
			.dealDate(LocalDate.of(2023, 4, 1))
			.addDetail("101", "0", LoanType.DEBIT, 1000)
			.addDetail("101", "0", LoanType.CREDIT, 1000)
			.build();
		JournalEntry entry2 = new JournalEntryTestFactory.Builder()
				.dealDate(LocalDate.of(2023, 4, 1))
				.addDetail("101", "0", LoanType.DEBIT, 5000)
				.addDetail("101", "0", LoanType.CREDIT, 5000)
				.build();
		JournalEntry entry3 = new JournalEntryTestFactory.Builder()
				.dealDate(LocalDate.of(2023, 4, 1))
				.addDetail("101", "0", LoanType.DEBIT, 3000)
				.addDetail("101", "0", LoanType.CREDIT, 3000)
				.build();
		journalEntryRepository.save(entry1, user.id());
		journalEntryRepository.save(entry2, user.id());
		journalEntryRepository.save(entry3, user.id());
		
		//when:仕訳合計金額をソートのキーに指定して仕訳データの一覧を取得する
		List<JournalEntryDto> data = fetchJournalEntryQueryService.fetchAll(YearMonth.of(2023, 4), JournalEntryOrderKey.TOTAL_AMOUNT, user.id());
		
		//then:取得結果が仕訳合計金額の降順で並べ替えられている
		//この場合、entry2 -> entry3 -> entry1 の順になっているはずである
		assertEquals(entry2.totalAmount().value(), data.get(0).getTotalAmount());
		assertEquals(entry3.totalAmount().value(), data.get(1).getTotalAmount());
		assertEquals(entry1.totalAmount().value(), data.get(2).getTotalAmount());
	}
	
}

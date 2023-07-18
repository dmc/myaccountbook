package wtbyt298.myaccountbook.application.usecase.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import wtbyt298.myaccountbook.domain.model.user.User;
import wtbyt298.myaccountbook.domain.shared.types.LoanType;
import wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;
import wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

@SpringBootTest
@Transactional
class CorrectJournalEntryUseCaseTest {
	
	@InjectMocks
	@Autowired
	private CorrectJournalEntryUseCase correctJournalEntryUseCase;
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Mock
	RegisterJournalEntryUseCase registerJournalEntryUseCase;
	
	@Mock
	private UserSession userSession;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//DBにテスト用データを投入
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		User user = userTestDataCreator.create();
		
		//依存オブジェクトの設定
		when(userSession.userId()).thenReturn(user.id());
	}
 	
	@Test
	void 削除対象の仕訳IDおよび仕訳登録用のDTOとユーザセッションを渡すと削除と登録が行われる() {
		//given:仕訳が作成され保存されている
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("101", "0", LoanType.DEBIT, 100)
			.addDetail("101", "0", LoanType.CREDIT, 100)
			.build();

		journalEntryRepository.save(entry, userSession.userId());

		//この時点では仕訳の存在チェックはtrueを返す
		assertTrue(journalEntryRepository.exists(entry.id()));
		
		//when:仕訳訂正処理を実行する
		List<RegisterEntryDetailCommand> details = List.of(
			new RegisterEntryDetailCommand("101", "0", "DEBIT", 100),
			new RegisterEntryDetailCommand("101", "0", "CREDIT", 100)
		);
		RegisterJournalEntryCommand command = new RegisterJournalEntryCommand(LocalDate.now(), "TEST", details);
		
		correctJournalEntryUseCase.execute(entry.id(), command, userSession);
		
		//then:仕訳が削除されている
		assertFalse(journalEntryRepository.exists(entry.id()));
	}
	
	@Test
	void 仕訳登録処理中に例外が発生すると仕訳取消処理もロールバックされ無効になる() {
		//given:仕訳が作成され保存されている
		JournalEntry entry = new JournalEntryTestFactory.Builder()
			.addDetail("101", "0", LoanType.DEBIT, 100)
			.addDetail("101", "0", LoanType.CREDIT, 100)
			.build();

		journalEntryRepository.save(entry, userSession.userId());
		
		//この時点では仕訳の存在チェックはtrueを返す
		assertTrue(journalEntryRepository.exists(entry.id()));
		
		//when:保存されている仕訳を削除し、別のデータを登録する
		//削除は成功するが、登録には失敗する
		List<RegisterEntryDetailCommand> details = List.of(
			new RegisterEntryDetailCommand("101", "0", "DEBIT", 100),
			new RegisterEntryDetailCommand("101", "0", "CREDIT", 100)
		);
		RegisterJournalEntryCommand command = new RegisterJournalEntryCommand(LocalDate.now(), "", details); //摘要が空白

		Exception exception = assertThrows(RuntimeException.class, () -> 
			correctJournalEntryUseCase.execute(entry.id(), command, userSession)
		);
		
		//then:ロールバックが実行され、仕訳は未だ存在している
		assertAll(
			() -> assertEquals("摘要が空白です。", exception.getMessage()),
			() -> assertTrue(journalEntryRepository.exists(entry.id()))
		);
	}
	
}

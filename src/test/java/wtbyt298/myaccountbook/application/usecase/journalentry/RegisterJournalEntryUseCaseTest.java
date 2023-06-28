package wtbyt298.myaccountbook.application.usecase.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import wtbyt298.myaccountbook.domain.model.user.User;
import wtbyt298.myaccountbook.domain.service.JournalEntryFactory;
import wtbyt298.myaccountbook.domain.service.JournalEntrySpecification;
import wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;

@SpringBootTest
@Transactional
class RegisterJournalEntryUseCaseTest {
	
	@Mock
	private JournalEntryRepository journalEntryRepository;
	
	@Mock
	private JournalEntrySpecification journalEntrySpecification;
	
	@Mock
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@Mock
	private UserSession userSession;

	@InjectMocks
	private RegisterJournalEntryUseCase registerUseCase;
	
	@Spy
	@Autowired
	private JournalEntryFactory journalEntryFactory;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//ユーザを作成する
		User user = userTestDataCreator.create("TEST_USER");
		when(userSession.userId()).thenReturn(user.id());
		
		//テストに必要な勘定科目を作成する
		accountTitleTestDataCreator.create("101", "現金", AccountingType.ASSETS);
		accountTitleTestDataCreator.create("102", "普通預金", AccountingType.ASSETS);
	}
	
	@Test
	void コマンドオブジェクトを渡すと渡した値で仕訳が作成されリポジトリへと引き渡される() {
		//given:
		ArgumentCaptor<JournalEntry> captor = ArgumentCaptor.forClass(JournalEntry.class);
		
		//仕訳の整合性は満たしている
		when(journalEntrySpecification.isSatisfied(any())).thenReturn(true);
		RegisterJournalEntryCommand command = createTestCommand();

		//when:コマンドオブジェクトを渡してテスト対象メソッドを実行する
		registerUseCase.execute(command, userSession);
		verify(journalEntryRepository).save(captor.capture(), any()); //リポジトリのsaveメソッドに渡される値をキャプチャする
		
		//then:コマンドオブジェクトの値をもとに仕訳が生成され、リポジトリに渡されている
		JournalEntry captured = captor.getValue();
		
		//仕訳のアサーションを行う
		assertAll(
			() -> assertEquals(command.getDealDate(), captured.dealDate().value()),
			() -> assertEquals(command.getDescription(), captured.description().value()),
			() -> assertEquals(command.getDetailCommands().size(), captured.entryDetails().size())
		);
		
		//子オブジェクトの仕訳明細のアサーションを行う
		//借方明細　101　0　DEBIT 1000
		RegisterEntryDetailCommand detailCommand1 = command.getDetailCommands().get(0);
		EntryDetail entryDetail1 = captured.entryDetails().get(0);
		assertAll(
			() -> assertEquals(detailCommand1.getAccountTitleId(), entryDetail1.accountTitleId().value()),
			() -> assertEquals(detailCommand1.getSubAccountTitleId(), entryDetail1.subAccountTitleId().value()),
			() -> assertEquals(detailCommand1.getDetailLoanType(), entryDetail1.detailLoanType().toString()),
			() -> assertEquals(detailCommand1.getAmount(), entryDetail1.amount().value())
		);
		
		//貸方明細　102　0　CREDIT 1000
		RegisterEntryDetailCommand detailCommand2 = command.getDetailCommands().get(1);
		EntryDetail entryDetail2 = captured.entryDetails().get(1);
		assertAll(
			() -> assertEquals(detailCommand2.getAccountTitleId(), entryDetail2.accountTitleId().value()),
			() -> assertEquals(detailCommand2.getSubAccountTitleId(), entryDetail2.subAccountTitleId().value()),
			() -> assertEquals(detailCommand2.getDetailLoanType(), entryDetail2.detailLoanType().toString()),
			() -> assertEquals(detailCommand2.getAmount(), entryDetail2.amount().value())
		);
	}
	
	@Test
	void 作成した仕訳が整合性を満たさない場合は例外発生() {
		//given:仕訳の整合性を満たしていない
		when(journalEntrySpecification.isSatisfied(any())).thenReturn(false);
		RegisterJournalEntryCommand command = createTestCommand();

		//when:コマンドオブジェクトを渡してテスト対象メソッドを実行する
		Exception exception = assertThrows(RuntimeException.class, () -> registerUseCase.execute(command, userSession));
		
		//then:想定した例外が発生している
		assertEquals("明細の貸借組み合わせが正しくありません。", exception.getMessage());
	}
	
	private RegisterJournalEntryCommand createTestCommand() {
		List<RegisterEntryDetailCommand> elements = new ArrayList<>();
		elements.add(new RegisterEntryDetailCommand("101", "0", "DEBIT", 1000));
		elements.add(new RegisterEntryDetailCommand("102", "0", "CREDIT", 1000));
		
		return new RegisterJournalEntryCommand(
			LocalDate.now(), 
			"仕訳登録のユースケースのテストです。", 
			elements
		);
	}

}

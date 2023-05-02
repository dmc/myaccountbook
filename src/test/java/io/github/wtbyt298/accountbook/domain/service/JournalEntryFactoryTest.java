package io.github.wtbyt298.accountbook.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.helper.testfactory.AccountTitleTestFactory;
import io.github.wtbyt298.accountbook.helper.testfactory.SubAccountTitlesTestFactory;

class JournalEntryFactoryTest {
	
	@Mock
	private AccountTitleRepository accountTitleRepository;
	
	@Mock
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Mock
	private UserSession userSession;
	
	@InjectMocks
	private JournalEntryFactory journalEntryFactory;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	void 仕訳登録用のコマンドオブジェクトを渡すと渡した値で仕訳のインスタンスが生成される() {
		//given:仕訳明細は貸借それぞれ1件ずつ
		RegisterJournalEntryCommand command = createCommand();
		//依存オブジェクトの設定
		when(accountTitleRepository.findById(any())).thenReturn(AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS));
		when(subAccountTitleRepository.findCollectionByParentId(any(), any())).thenReturn(SubAccountTitlesTestFactory.create());
		
		//when:テスト対象メソッドを実行して仕訳のインスタンスを生成する
		JournalEntry created = journalEntryFactory.create(command, userSession.userId());
		
		//then:渡された値でインスタンスが生成されている
		assertEquals(command.getDealDate(), created.dealDate().value());
		assertEquals(command.getDescription(), created.description().value());
		assertEquals(command.getDetailCommands().size(), created.entryDetails().size());
		//子要素である仕訳明細のアサーションを行う
		for (int i = 0; i < command.getDetailCommands().size(); i++) {
			RegisterEntryDetailCommand debitDetailCommand = command.getDetailCommands().get(i);
			EntryDetail createdDebitDetail = created.entryDetails().get(i);
			assertEquals(debitDetailCommand.getAccountTitleId(), createdDebitDetail.accountTitleId().value());
			assertEquals(debitDetailCommand.getSubAccountTitleId(), createdDebitDetail.subAccountTitleId().value());
			assertEquals(debitDetailCommand.getDetailLoanType(), createdDebitDetail.detailLoanType());
			assertEquals(debitDetailCommand.getAmount(), createdDebitDetail.amount().value());
		}
	}
	
	/**
	 * テスト用の仕訳登録コマンドオブジェクトを生成する
	 */
	private RegisterJournalEntryCommand createCommand() {
		List<RegisterEntryDetailCommand> list = new ArrayList<>();
		list.add(new RegisterEntryDetailCommand("102", "0", "DEBIT", 100));
		list.add(new RegisterEntryDetailCommand("102", "1", "CREDIT", 100));
		return new RegisterJournalEntryCommand(
			LocalDate.now(), 
			"ファクトリのテストです。", 
			list
		);
	}

}

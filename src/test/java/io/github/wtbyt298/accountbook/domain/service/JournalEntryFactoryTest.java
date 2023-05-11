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
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;

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
		//given:仕訳明細は借方2件、貸方1件の計3件
		RegisterJournalEntryCommand command = createCommand();
		//依存オブジェクトの設定
		when(accountTitleRepository.exists(any())).thenReturn(true);
		when(subAccountTitleRepository.exists(any(), any(), any())).thenReturn(true);
		
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
			assertEquals(debitDetailCommand.getDetailLoanType().toString(), createdDebitDetail.detailLoanType().toString());
			assertEquals(debitDetailCommand.getAmount(), createdDebitDetail.amount().value());
		}
	}
	
	@Test
	void 勘定科目または補助科目が存在しない場合は例外が発生する() {
		//given:コマンドに該当する勘定科目は存在しているが、補助科目が存在しない
		RegisterJournalEntryCommand command = createCommand();
		when(accountTitleRepository.exists(any())).thenReturn(true);
		when(subAccountTitleRepository.exists(any(), any(), any())).thenReturn(false);
		
		//when:テスト対象メソッドを実行して仕訳のインスタンスを生成する
		Exception exception = assertThrows(RuntimeException.class, () -> journalEntryFactory.create(command, userSession.userId()));
		
		//then:想定した例外が発生している
		assertEquals("指定した補助科目が存在しないため仕訳を作成できません。", exception.getMessage());
	}
	
	/**
	 * テスト用の仕訳登録コマンドオブジェクトを生成する
	 */
	private RegisterJournalEntryCommand createCommand() {
		List<RegisterEntryDetailCommand> commandList = new ArrayList<>();
		commandList.add(new RegisterEntryDetailCommand("102", "0", "DEBIT", 1000));
		commandList.add(new RegisterEntryDetailCommand("102", "0", "DEBIT", 2000));
		commandList.add(new RegisterEntryDetailCommand("102", "1", "CREDIT", 3000));
		return new RegisterJournalEntryCommand(
			LocalDate.now(), 
			"ファクトリのテストです。", 
			commandList
		);
	}

}

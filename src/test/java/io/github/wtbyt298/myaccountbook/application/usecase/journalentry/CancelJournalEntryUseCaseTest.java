package io.github.wtbyt298.myaccountbook.application.usecase.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.myaccountbook.helper.testfactory.JournalEntryTestFactory;

class CancelJournalEntryUseCaseTest {
	
	@Mock
	private JournalEntryRepository journalEntryRepository;
	
	@Mock
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@Mock
	private UserSession userSession;
	
	@InjectMocks
	private CancelJournalEntryUseCase cancelUseCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void 仕訳IDを渡すと該当する仕訳が削除される() {
		//given:仕訳IDに該当する仕訳は存在している
		ArgumentCaptor<EntryId> captor = ArgumentCaptor.forClass(EntryId.class);
		JournalEntry entry = new JournalEntryTestFactory.Builder().build();
		when(journalEntryRepository.exists(any())).thenReturn(true);
		when(journalEntryRepository.findById(any())).thenReturn(entry);
		
		//when:仕訳IDを指定してテスト対象メソッドを実行する
		EntryId entryId = EntryId.fromString("TEST");
		cancelUseCase.execute(entryId, userSession);
		verify(journalEntryRepository).drop(captor.capture()); //リポジトリのdropメソッドに渡される値をキャプチャする
		
		//then:指定した仕訳IDがリポジトリに渡されている
		EntryId capturedId = captor.getValue();
		assertEquals(entryId, capturedId);
	}

	@Test
	void 仕訳IDに該当する仕訳が見つからない場合は例外発生() {
		//given:仕訳IDに該当する仕訳は存在しない
		when(journalEntryRepository.exists(any())).thenReturn(false);
		
		//when:仕訳IDを渡してテスト対象メソッドを実行する
		EntryId entryId = EntryId.fromString("TEST");
		Exception exception = assertThrows(RuntimeException.class, () -> cancelUseCase.execute(entryId, userSession));
		
		//then:想定した例外が発生している
		assertEquals("指定した仕訳は存在しません。", exception.getMessage());
	}
	
}

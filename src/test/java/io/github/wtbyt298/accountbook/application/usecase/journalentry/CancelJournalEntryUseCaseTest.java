package io.github.wtbyt298.accountbook.application.usecase.journalentry;

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
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;

class CancelJournalEntryUseCaseTest {
	
	@Mock
	private JournalEntryRepository journalEntryRepository;
	
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
		when(journalEntryRepository.exists(any())).thenReturn(true);
		
		//when:仕訳IDを指定してテスト対象メソッドを実行する
		EntryId entryId = EntryId.fromString("TEST");
		cancelUseCase.execute(entryId);
		verify(journalEntryRepository).drop(captor.capture()); //リポジトリのdropメソッドに渡される値をキャプチャする
		
		//then:指定した仕訳IDがリポジトリに渡されている
		EntryId capturedId = captor.getValue();
		assertEquals(entryId, capturedId);
	}

	@Test
	void 仕訳IDに該当する仕訳が見つからない場合は例外発生() {
		//given:仕訳IDに該当する仕訳は存在しない
		when(journalEntryRepository.exists(any())).thenReturn(false);
		
		//when:新規生成した仕訳IDを渡してテスト対象メソッドを実行する
		EntryId entryId = EntryId.newInstance();
		Exception exception = assertThrows(IllegalArgumentException.class, () -> cancelUseCase.execute(entryId));
		
		//then:想定した例外が発生している
		assertEquals("指定した仕訳は存在しません。", exception.getMessage());
	}
	
}

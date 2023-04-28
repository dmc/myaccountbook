package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import static org.junit.jupiter.api.Assertions.*;
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
		//given
		ArgumentCaptor<EntryId> captor = ArgumentCaptor.forClass(EntryId.class);
		EntryId entryId = EntryId.fromString("TEST");
		when(journalEntryRepository.exists(entryId)).thenReturn(true); //仕訳IDに該当する仕訳は存在している
		
		//when
		cancelUseCase.execute(entryId);
		verify(journalEntryRepository).drop(captor.capture()); //リポジトリのdropメソッドに渡される値をキャプチャする
		
		//then
		EntryId capturedId = captor.getValue();
		assertEquals(entryId, capturedId);
	}

	@Test
	void 仕訳IDに該当する仕訳が見つからない場合は例外発生() {
		//given
		EntryId entryId = EntryId.newInstance();
		when(journalEntryRepository.exists(entryId)).thenReturn(false); //仕訳IDに該当する仕訳は存在しない
		
		//when
		Exception exception = assertThrows(IllegalArgumentException.class, () -> cancelUseCase.execute(entryId));
		
		//then
		assertEquals("指定した仕訳は存在しません。", exception.getMessage());
	}
	
}

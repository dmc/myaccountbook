package wtbyt298.myaccountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryId;

/**
 * 仕訳訂正処理クラス
 */
@Service
public class CorrectJournalEntryUseCase {

	@Autowired
	private CancelJournalEntryUseCase cancelJournalEntryUseCase;
	
	@Autowired
	private RegisterJournalEntryUseCase registerJournalEntryUseCase;

	/**
	 * 仕訳を訂正する
	 * 作成済の仕訳を削除し、新しい仕訳を登録する
	 * @param entryId 削除対象の仕訳ID
	 * @param command 仕訳登録用のDTO
	 */
	@Transactional
	public void execute(EntryId entryId, RegisterJournalEntryCommand command, UserSession userSession) {
		cancelJournalEntryUseCase.execute(entryId, userSession);
		registerJournalEntryUseCase.execute(command, userSession);
	}
	
}

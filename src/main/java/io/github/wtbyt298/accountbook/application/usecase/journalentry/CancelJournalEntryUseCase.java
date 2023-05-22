package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.shared.AccountBalanceUpdator;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;

/**
 * 仕訳取消処理クラス
 */
@Service
public class CancelJournalEntryUseCase {
	
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountBalanceUpdator accountBalanceUpdator;
	
	/**
	 * 仕訳を取消（削除）する
	 * @param entryId 取消対象の仕訳ID
	 */
	@Transactional
	public void execute(EntryId entryId, UserSession userSession) {
		if (! journalEntryRepository.exists(entryId)) {
			throw new UseCaseException("指定した仕訳は存在しません。");
		}
		JournalEntry entry = journalEntryRepository.findById(entryId);
		UserId userId = userSession.userId();
		//貸借を入れ替えた仕訳を生成し、残高更新サービスに引き渡す
		JournalEntry reversingEntry = entry.toReversingJournalEntry();
		accountBalanceUpdator.execute(reversingEntry, userId);
		//元の仕訳を削除する
		journalEntryRepository.drop(entryId);
	}
	
}

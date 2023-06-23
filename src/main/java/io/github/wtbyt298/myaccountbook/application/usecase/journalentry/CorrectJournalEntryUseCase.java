package io.github.wtbyt298.myaccountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.JournalEntryRepository;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import io.github.wtbyt298.myaccountbook.domain.service.JournalEntryFactory;
import io.github.wtbyt298.myaccountbook.domain.service.JournalEntrySpecification;
import io.github.wtbyt298.myaccountbook.domain.shared.exception.CannotCreateJournalEntryException;

/**
 * 仕訳訂正処理クラス
 */
@Service
public class CorrectJournalEntryUseCase {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@Autowired
	private JournalEntryFactory journalEntryFactory;
	
	@Autowired
	private JournalEntrySpecification journalEntrySpecification;
	
	/**
	 * 仕訳を訂正する
	 * 作成済の仕訳を削除し、新しい仕訳を登録する
	 * @param entryId 削除対象の仕訳ID
	 * @param command 仕訳登録用のDTO
	 */
	@Transactional
	public void execute(EntryId entryId, RegisterJournalEntryCommand command, UserSession userSession) {
		UserId userId = userSession.userId();
		cancel(entryId, userId);
		register(command, userId);
	}
	
	/**
	 * 仕訳を削除する
	 */
	private void cancel(EntryId entryId, UserId userId) {
		if (! journalEntryRepository.exists(entryId)) {
			throw new UseCaseException("指定した仕訳は存在しません。");
		}
		
		//ドメインオブジェクトを生成
		JournalEntry entry = journalEntryRepository.findById(entryId);
		
		//貸借を入れ替えた仕訳を生成し、残高更新サービスに引き渡す
		JournalEntry reversingEntry = entry.toReversingJournalEntry();
		accountBalanceUpdator.execute(reversingEntry, userId);
		
		//元の仕訳を削除する
		journalEntryRepository.drop(entryId);
	}
	
	/**
	 * 仕訳を登録する
	 */
	private void register(RegisterJournalEntryCommand command, UserId userId) {
		//ドメインオブジェクトを生成
		JournalEntry entry = journalEntryFactory.create(command, userId);
		
		//仕訳の整合性チェック
		if (! journalEntrySpecification.isSatisfied(entry)) {
			throw new CannotCreateJournalEntryException("明細の貸借組み合わせが正しくありません。");
		}
		
		//残高更新サービスに引き渡し、リポジトリに保存する
		accountBalanceUpdator.execute(entry, userId);
		journalEntryRepository.save(entry, userId);
	}
	
}

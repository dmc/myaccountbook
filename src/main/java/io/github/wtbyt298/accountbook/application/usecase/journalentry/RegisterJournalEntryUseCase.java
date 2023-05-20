package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.shared.AccountBalanceUpdator;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.service.JournalEntryFactory;
import io.github.wtbyt298.accountbook.domain.service.JournalEntrySpecification;
import io.github.wtbyt298.accountbook.domain.shared.exception.DomainException;

/**
 * 仕訳登録処理クラス
 */
@Service
public class RegisterJournalEntryUseCase {

	@Autowired
	private JournalEntryRepository journalEntryRepository;
	
	@Autowired
	private AccountBalanceUpdator accountBalanceUpdator;
	
	@Autowired
	private JournalEntryFactory journalEntryFactory;
	
	@Autowired
	private JournalEntrySpecification journalEntrySpecification;
	
	/**
	 * 仕訳を登録する
	 * @param 仕訳登録用のDTO
	 */
	@Transactional
	public void execute(RegisterJournalEntryCommand command, UserSession userSession) {
		UserId userId = userSession.userId();
		JournalEntry entry = journalEntryFactory.create(command, userId);
		if (! journalEntrySpecification.isSatisfied(entry)) {
			throw new DomainException("明細の貸借組み合わせが正しくありません。");
		}
		//残高更新サービスに引き渡し、リポジトリに保存する
		accountBalanceUpdator.execute(entry, userId);
		journalEntryRepository.save(entry, userId);
	}
	
}

package com.github.wtbyt298.myaccountbook.application.usecase.journalentry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import com.github.wtbyt298.myaccountbook.application.usecase.shared.AccountBalanceUpdator;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.*;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import com.github.wtbyt298.myaccountbook.domain.service.JournalEntryFactory;
import com.github.wtbyt298.myaccountbook.domain.service.JournalEntrySpecification;
import com.github.wtbyt298.myaccountbook.domain.shared.exception.CannotCreateJournalEntryException;

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
		//ドメインオブジェクトを生成
		UserId userId = userSession.userId();
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

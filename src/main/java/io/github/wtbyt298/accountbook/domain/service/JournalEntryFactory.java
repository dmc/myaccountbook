package io.github.wtbyt298.accountbook.domain.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.exception.DomainException;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 仕訳のファクトリクラス
 * 仕訳の新規作成時の詳細な処理を行う
 */
@Component
public class JournalEntryFactory {
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	/**
	 * コマンドオブジェクトから仕訳を生成する
	 */
	public JournalEntry create(RegisterJournalEntryCommand command, UserId userId) {
		List<EntryDetail> elements = command.getDetailCommands().stream()
			.map(each -> mapCommandToEntity(each, userId))
			.toList();
		return JournalEntry.create(
			DealDate.valueOf(command.getDealDate()), 
			Description.valueOf(command.getDescription()), 
			new EntryDetails(elements)
		);
	}
	
	/**
	 * コマンドオブジェクトをエンティティに詰め替える
	 */
	private EntryDetail mapCommandToEntity(RegisterEntryDetailCommand command, UserId userId) {
		AccountTitleId accountTitleId = AccountTitleId.valueOf(command.getAccountTitleId());
		SubAccountTitleId subAccountTitleId = SubAccountTitleId.valueOf(command.getSubAccountTitleId());
		if (! accountTitleRepository.exists(accountTitleId)) {
			throw new DomainException("指定した勘定科目が存在しないため仕訳を作成できません。");
		}
		if (! subAccountTitleRepository.exists(accountTitleId, subAccountTitleId, userId)) {
			throw new DomainException("指定した補助科目が存在しないため仕訳を作成できません。");
		}
		return new EntryDetail(
			accountTitleId,
			subAccountTitleId,
			LoanType.valueOf(command.getDetailLoanType()),
			Amount.valueOf(command.getAmount())
		);
	}
	
}

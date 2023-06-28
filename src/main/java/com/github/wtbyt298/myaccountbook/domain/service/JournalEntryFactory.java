package com.github.wtbyt298.myaccountbook.domain.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.wtbyt298.myaccountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import com.github.wtbyt298.myaccountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import com.github.wtbyt298.myaccountbook.domain.model.journalentry.*;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import com.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import com.github.wtbyt298.myaccountbook.domain.shared.exception.DomainException;
import com.github.wtbyt298.myaccountbook.domain.shared.types.LoanType;

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
		//勘定科目の存在チェック
		AccountTitleId accountTitleId = AccountTitleId.valueOf(command.getAccountTitleId());
		if (! accountTitleRepository.exists(accountTitleId)) {
			throw new DomainException("指定した勘定科目が存在しないため仕訳を作成できません。");
		}
		
		//補助科目の存在チェック
		SubAccountTitleId subAccountTitleId = SubAccountTitleId.valueOf(command.getSubAccountTitleId());
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

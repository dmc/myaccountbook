package io.github.wtbyt298.accountbook.domain.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

//TODO コードが汚くなっているので設計の見直しを行う

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
	 * 仕訳登録用のコマンドオブジェクトから仕訳を作成する
	 */
	public JournalEntry create(RegisterJournalEntryCommand command, UserId userId) {
		//ドメインオブジェクトの生成処理
		DealDate dealDate = DealDate.valueOf(command.getDealDate());
		Description description = Description.valueOf(command.getDescription());
		EntryDetails entryDetail = createEntryDetail(command.getDetailCommands(), userId);
		//仕訳IDはファクトリメソッド内で新規生成している
		return JournalEntry.create(dealDate, description, entryDetail);
	}
	
	/**
	 * コマンドオブジェクトから仕訳明細を作成する
	 */
	private EntryDetails createEntryDetail(List<RegisterEntryDetailCommand> commands, UserId userId) {
		List<EntryDetail> detailRows = new ArrayList<>();
		for (RegisterEntryDetailCommand command : commands) {
			detailRows.add(createDetailRow(command, userId));
		}
		return new EntryDetails(detailRows);
	}
	
	/**
	 * 1件の明細コマンドオブジェクトから明細行を作成する
	 */
	private EntryDetail createDetailRow(RegisterEntryDetailCommand command, UserId userId) {
		//勘定科目に関する処理
		AccountTitleId accountTitleId = AccountTitleId.valueOf(command.getAccountTitleId());
		AccountTitle accountTitle = accountTitleRepository.findById(accountTitleId);
		//補助科目に関する処理
		SubAccountTitleId subAccountTitleId = SubAccountTitleId.valueOf(command.getSubAccountTitleId());
		SubAccountTitles subAccountTitles = subAccountTitleRepository.findCollectionByParentId(accountTitleId, userId);
		SubAccountTitle subAccountTitle = subAccountTitles.find(subAccountTitleId);
		//その他の処理
		LoanType detailLoanType = command.getDetailLoanType();
		Amount amount = Amount.valueOf(command.getAmount());
		//エンティティを返す
		return new EntryDetail(accountTitle, subAccountTitle, detailLoanType, amount);
	}
	
}

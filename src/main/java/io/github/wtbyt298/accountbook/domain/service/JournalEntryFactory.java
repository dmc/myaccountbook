package io.github.wtbyt298.accountbook.domain.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.EntryDetailRegisterCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.JournalEntryRegisterCommand;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.model.shared.Amount;
import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;

/**
 * 仕訳のファクトリクラス
 * 仕訳の新規作成時の詳細な処理を行う
 */
@Service
public class JournalEntryFactory {
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	/**
	 * 仕訳登録用のコマンドオブジェクトから仕訳を作成する
	 */
	public JournalEntry create(JournalEntryRegisterCommand command) {
		DealDate dealDate = DealDate.valueOf(command.getDealDate());
		Description description = Description.valueOf(command.getDescription());
		EntryDetail entryDetail = createEntryDetail(command.getDetailCommands());
		return JournalEntry.create(dealDate, description, entryDetail);
	}
	
	/**
	 * コマンドオブジェクトから仕訳明細を作成する
	 */
	private EntryDetail createEntryDetail(List<EntryDetailRegisterCommand> commands) {
		List<DetailRow> detailRows = new ArrayList<>();
		for (EntryDetailRegisterCommand command : commands) {
			detailRows.add(createDetailRow(command));
		}
		return new EntryDetail(detailRows);
	}
	
	/**
	 * 1件の明細コマンドオブジェクトから明細行を作成する
	 */
	private DetailRow createDetailRow(EntryDetailRegisterCommand command) {
		//勘定科目に関する処理
		AccountTitleId accountTitleId = AccountTitleId.valueOf(command.getAccountTitleId());
		SubAccountTitleId subAccountTitleId = SubAccountTitleId.valueOf(command.getSubAccountTitleId());
		AccountTitle accountTitle = accountTitleRepository.findById(accountTitleId);
		SubAccountTitle subAccountTitle = accountTitle.findChild(subAccountTitleId);
		//その他の処理
		LoanType detailLoanType = LoanType.valueOf(command.getDetailLoanType());
		Amount amount = Amount.valueOf(command.getAmount());
		//エンティティを返す
		return new DetailRow(accountTitle, subAccountTitle, detailLoanType, amount);
	}
	
}

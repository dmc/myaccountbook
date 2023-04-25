package io.github.wtbyt298.accountbook.domain.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterEntryDetailCommand;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryCommand;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.journalentry.*;
import io.github.wtbyt298.accountbook.domain.model.shared.Amount;
import io.github.wtbyt298.accountbook.domain.model.shared.types.LoanType;

//TODO コードが汚くなっているので設計の見直しを行う

/**
 * 仕訳のファクトリクラス
 * 仕訳の新規作成時の詳細な処理を行う
 */
@Component
public class JournalEntryFactory {
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	/**
	 * 仕訳登録用のコマンドオブジェクトから仕訳を作成する
	 */
	public JournalEntry create(RegisterJournalEntryCommand command) {
		//ドメインオブジェクトの生成処理
		DealDate dealDate = DealDate.valueOf(command.getDealDate());
		Description description = Description.valueOf(command.getDescription());
		EntryDetail entryDetail = createEntryDetail(command.getDetailCommands());
		//仕訳IDはファクトリメソッド内で新規生成している
		return JournalEntry.create(dealDate, description, entryDetail);
	}
	
	/**
	 * コマンドオブジェクトから仕訳明細を作成する
	 */
	private EntryDetail createEntryDetail(List<RegisterEntryDetailCommand> commands) {
		List<DetailRow> detailRows = new ArrayList<>();
		for (RegisterEntryDetailCommand command : commands) {
			detailRows.add(createDetailRow(command));
		}
		return new EntryDetail(detailRows);
	}
	
	/**
	 * 1件の明細コマンドオブジェクトから明細行を作成する
	 */
	private DetailRow createDetailRow(RegisterEntryDetailCommand command) {
		//勘定科目に関する処理
		AccountTitleId accountTitleId = AccountTitleId.valueOf(command.getAccountTitleId());
		AccountTitle accountTitle = accountTitleRepository.findById(accountTitleId);
		//補助科目に関する処理
		SubAccountTitleId subAccountTitleId = SubAccountTitleId.valueOf(command.getSubAccountTitleId());
		SubAccountTitle subAccountTitle = accountTitle.findChild(subAccountTitleId);
		//その他の処理
		LoanType detailLoanType = LoanType.valueOf(command.getDetailLoanType());
		Amount amount = Amount.valueOf(command.getAmount());
		//エンティティを返す
		return new DetailRow(accountTitle, subAccountTitle, detailLoanType, amount);
	}
	
}

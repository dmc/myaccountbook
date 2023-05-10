package io.github.wtbyt298.accountbook.application.usecase.shared;

import java.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.account.AccountingTransaction;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryDetail;
import io.github.wtbyt298.accountbook.domain.model.journalentry.JournalEntry;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 残高更新処理クラス
 */
@Component
public class AccountBalanceUpdator {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	/**
	 * 仕訳を受け取って各勘定の残高を更新する
	 */
	public void execute(JournalEntry entry, UserId userId) {
		//明細1件ごとに勘定を生成し、残高を更新する
		for (EntryDetail each : entry.entryDetails()) {
			Account account = createAccountFrom(each, userId, entry.fiscalYearMonth());
			AccountingTransaction adding = new AccountingTransaction(each.detailLoanType(), each.amount());
			Account updated = account.addTransaction(adding);
			accountRepository.save(updated, userId);
		}
	}
	
	/**
	 * 勘定のインスタンスを組み立てる
	 */
	private Account createAccountFrom(EntryDetail entryDetail, UserId userId, YearMonth fiscalYearMonth) {
		AccountTitleId accountTitleId = entryDetail.accountTitleId();
		return accountRepository.find(
			accountTitleRepository.findById(accountTitleId), 
			entryDetail.subAccountTitleId(), 
			userId, 
			fiscalYearMonth
		);
	}

}

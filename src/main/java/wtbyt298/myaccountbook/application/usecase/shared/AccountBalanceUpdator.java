package wtbyt298.myaccountbook.application.usecase.shared;

import java.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import wtbyt298.myaccountbook.domain.model.account.Account;
import wtbyt298.myaccountbook.domain.model.account.AccountRepository;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.journalentry.EntryDetail;
import wtbyt298.myaccountbook.domain.model.journalentry.JournalEntry;
import wtbyt298.myaccountbook.domain.model.user.UserId;

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
		//仕訳明細1件ごとに勘定オブジェクトを生成し、残高更新を行う
		for (EntryDetail each : entry.entryDetails()) {
			Account account = createAccountFrom(each, userId, entry.fiscalYearMonth());
			
			//残高を更新し、リポジトリに保存する
			Account updated = account.updateBalance(each.detailLoanType(), each.amount());
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

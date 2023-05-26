package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.account;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import static generated.tables.JournalEntries.*;
import static generated.tables.EntryDetails.*;
import static generated.tables.MonthlyBalances.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.account.AccountingTransaction;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.journalentry.Amount;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.LoanType;

/**
 * 勘定のリポジトリクラス
 * 勘定集約の永続化と再構築の詳細を記述する
 */
@Repository
public class AccountJooqRepository implements AccountRepository {

	@Autowired
	private DSLContext jooq;

	/**
	 * 勘定の残高を保存する
	 * 該当月のレコードを一旦削除し、再度INSERTする
	 */
	@Override
	public void save(Account account, UserId userId) {
		deleteRecord(account, userId);
		insertRecord(account, userId);
	}
	
	private void insertRecord(Account account, UserId userId) {
		jooq.insertInto(MONTHLY_BALANCES)
			.set(MONTHLY_BALANCES.ACCOUNTTITLE_ID, account.accountTitleId().value())
			.set(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID, account.subAccountTitleId().value())
			.set(MONTHLY_BALANCES.USER_ID, userId.value())
			.set(MONTHLY_BALANCES.FISCAL_YEARMONTH, account.fiscalYearMonth().toString())
			.set(MONTHLY_BALANCES.BALANCE, account.balance())
			.execute();
	}
	
	private void deleteRecord(Account account, UserId userId) {
		jooq.deleteFrom(MONTHLY_BALANCES)
			.where(MONTHLY_BALANCES.ACCOUNTTITLE_ID.eq(account.accountTitleId().value()))
				.and(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID.eq(account.subAccountTitleId().value()))
				.and(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.eq(account.fiscalYearMonth().toString()))
			.execute();
	}
	
	/**
	 * 勘定オブジェクトを取得する
	 */
	@Override
	public Account find(AccountTitle accountTitle, SubAccountTitleId subId, UserId userId, YearMonth fiscalYearMonth) {
		List<AccountingTransaction> transactionHistory = new ArrayList<>();
		Result<Record> result = jooq.select()
									.from(JOURNAL_ENTRIES, ENTRY_DETAILS)
									.where(JOURNAL_ENTRIES.ENTRY_ID.eq(ENTRY_DETAILS.ENTRY_ID))
										.and(ENTRY_DETAILS.ACCOUNTTITLE_ID.eq(accountTitle.id().value()))
										.and(ENTRY_DETAILS.SUB_ACCOUNTTITLE_ID.eq(subId.value()))
										.and(JOURNAL_ENTRIES.USER_ID.eq(userId.value()))
										.and(JOURNAL_ENTRIES.FISCAL_YEARMONTH.eq(fiscalYearMonth.toString()))
									.fetch();
		for (Record each : result) {
			AccountingTransaction accountingTransaction = new AccountingTransaction(
				LoanType.valueOf(each.get(ENTRY_DETAILS.LOAN_TYPE)), 
				Amount.valueOf(each.get(ENTRY_DETAILS.AMOUNT))
			);
			transactionHistory.add(accountingTransaction);
		}
		return new Account(accountTitle, subId, fiscalYearMonth, transactionHistory);
	}

}

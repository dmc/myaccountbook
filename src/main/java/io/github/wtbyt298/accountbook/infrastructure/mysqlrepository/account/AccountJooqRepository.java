package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.account;

import java.time.YearMonth;
import static generated.tables.MonthlyBalances.*;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.wtbyt298.accountbook.domain.model.account.Account;
import io.github.wtbyt298.accountbook.domain.model.account.AccountRepository;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

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
	 */
	@Override
	public void save(Account account, UserId userId) {
		//該当月のレコードを一旦削除し、再度INSERTする
		deleteRecord(account, userId);
		insertRecord(account, userId);
	}
	
	/**
	 * 勘定のデータを保存する
	 */
	private void insertRecord(Account account, UserId userId) {
		jooq.insertInto(MONTHLY_BALANCES)
			.set(MONTHLY_BALANCES.ACCOUNTTITLE_ID, account.accountTitleId().value())
			.set(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID, account.subAccountTitleId().value())
			.set(MONTHLY_BALANCES.USER_ID, userId.value())
			.set(MONTHLY_BALANCES.FISCAL_YEARMONTH, account.fiscalYearMonth().toString())
			.set(MONTHLY_BALANCES.BALANCE, account.balance())
			.execute();
	}
	
	/**
	 * 勘定のデータを削除する
	 */
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
		Record record = jooq.select()
			.from(MONTHLY_BALANCES)
			.where(MONTHLY_BALANCES.ACCOUNTTITLE_ID.eq(accountTitle.id().value()))
				.and(MONTHLY_BALANCES.SUB_ACCOUNTTITLE_ID.eq(subId.value()))
				.and(MONTHLY_BALANCES.USER_ID.eq(userId.value()))
				.and(MONTHLY_BALANCES.FISCAL_YEARMONTH.eq(fiscalYearMonth.toString()))
			.fetchOne();
		
		return mapRecordToEntity(accountTitle, subId, fiscalYearMonth, record);
	}
	
	/**
	 * レコードをエンティティに詰め替える
	 */
	private Account mapRecordToEntity(AccountTitle accountTitle, SubAccountTitleId subId, YearMonth fiscalYearMonth, Record record) {
		//レコードが存在しない場合は、残高0の勘定を返す
		if (record == null) {
			return new Account(accountTitle, subId, fiscalYearMonth, 0);
		}
		final int balance = record.get(MONTHLY_BALANCES.BALANCE);
		return new Account(accountTitle, subId, fiscalYearMonth, balance);
	}

}

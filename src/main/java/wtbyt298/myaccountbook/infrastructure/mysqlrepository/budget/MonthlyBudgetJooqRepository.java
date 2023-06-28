package wtbyt298.myaccountbook.infrastructure.mysqlrepository.budget;

import static generated.tables.Budgets.*;
import static generated.tables.Accounttitles.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.budget.MonthlyBudget;
import wtbyt298.myaccountbook.domain.model.budget.MonthlyBudgetRepository;
import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 予算のリポジトリクラス
 * 予算集約の永続化と再構築の詳細を記述する
 */
@Repository
class MonthlyBudgetJooqRepository implements MonthlyBudgetRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 設定した予算額を保存する
	 */
	@Override
	public void save(MonthlyBudget monthlyBudget, AccountTitleId accountTitleId, UserId userId) {
		//一旦レコードを削除し、再度INSERTする
		deleteRecord(accountTitleId, userId);
		insertRecord(monthlyBudget, accountTitleId, userId);
	}
	
	/**
	 * 予算テーブルにデータを挿入する
	 */
	private void insertRecord(MonthlyBudget monthlyBudget, AccountTitleId accountTitleId, UserId userId) {
		jooq.insertInto(BUDGETS)
			.set(BUDGETS.ACCOUNTTITLE_ID, accountTitleId.value())
			.set(BUDGETS.USER_ID, userId.value())
			.set(BUDGETS.BUDGET_AMOUNT, monthlyBudget.value())
			.execute();
	}
	
	/**
	 * 予算テーブルからデータを削除する
	 */
	private void deleteRecord(AccountTitleId accountTitleId, UserId userId) {
		jooq.deleteFrom(BUDGETS)
			.where(BUDGETS.ACCOUNTTITLE_ID.eq(accountTitleId.value()))
				.and(BUDGETS.USER_ID.eq(userId.value()))
			.execute();
	}
	
	/**
	 * 勘定科目ごとの予算データの一覧を取得する
	 */
	@Override
	public Map<AccountTitleId, MonthlyBudget> findAll(UserId userId) {
		Result<Record> result = jooq.select()
			.from(ACCOUNTTITLES)
			.leftOuterJoin(BUDGETS)
				.on(ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(BUDGETS.ACCOUNTTITLE_ID))
				.and(BUDGETS.USER_ID.eq(userId.value()))
			.where(ACCOUNTTITLES.ACCOUNTING_TYPE.eq(AccountingType.EXPENSES.toString()))
			.fetch();
		
		//レコードをMapに詰め替える
		Map<AccountTitleId, MonthlyBudget> mapOfEntity = new LinkedHashMap<>();
		for (Record each : result) {
			//予算が未設定の場合、予算額はnullになる可能性あり
			AccountTitleId accountTitleId = AccountTitleId.valueOf(each.get(ACCOUNTTITLES.ACCOUNTTITLE_ID));
			MonthlyBudget monthlyBudget = MonthlyBudget.valueOf(Optional.ofNullable(each.get(BUDGETS.BUDGET_AMOUNT)).orElse(0));
			mapOfEntity.put(accountTitleId, monthlyBudget);
		}
		
		return mapOfEntity;
	}
	
	/**
	 * 予算データが存在するかどうかを判断する
	 */
	@Override
	public boolean exists(AccountTitleId accountTitleId, UserId userId) {
		final int resultCount = jooq.select()
			.from(BUDGETS)
			.where(BUDGETS.ACCOUNTTITLE_ID.eq(accountTitleId.value()))
				.and(BUDGETS.USER_ID.eq(userId.value()))
			.execute();
		
		if (resultCount == 0) return false;
		return true;
	}

}

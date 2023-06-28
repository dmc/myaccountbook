package wtbyt298.myaccountbook.application.usecase.budget;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.budget.MonthlyBudget;
import wtbyt298.myaccountbook.domain.model.budget.MonthlyBudgetRepository;
import wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 予算設定処理クラス
 */
@Service
public class ConfigureBudgetUseCase {

	@Autowired
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	/**
	 * 予算を設定する
	 */
	@Transactional
	public void execute(Map<String, Integer> configureBudgetCommand, UserSession userSession) {
		UserId userId = userSession.userId();
		
		for (Entry<String, Integer> each : configureBudgetCommand.entrySet()) {
			//勘定科目ごとにドメインオブジェクトを生成する
			AccountTitleId accountTitleId = AccountTitleId.valueOf(each.getKey());
			if (! isExpenses(accountTitleId)) {
				throw new UseCaseException("費用の科目以外には予算を設定できません。");
			}
			
			//予算額がゼロの場合、データが登録済みであれば上書きする
			MonthlyBudget monthlyBudget = MonthlyBudget.valueOf(each.getValue());
			if (monthlyBudget.isZero()) {
				if (! monthlyBudgetRepository.exists(accountTitleId, userId)) continue;
			}
			
			//リポジトリに保存する
			monthlyBudgetRepository.save(monthlyBudget, accountTitleId, userId);
		}
	}
	
	/**
	 * 渡された勘定科目IDが費用の科目のものかを判断する
	 */
	private boolean isExpenses(AccountTitleId accountTitleId) {
		AccountTitle accountTitle = accountTitleRepository.findById(accountTitleId);
		
		if (accountTitle.accountingType().equals(AccountingType.EXPENSES)) {
			return true;
		}
		return false;
	}
	
}

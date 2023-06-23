package io.github.wtbyt298.accountbook.application.usecase.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudget;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudgetRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 予算比の計算処理クラス
 */
@Service
public class CalculateRatioOfBudgetUseCase {

	@Autowired
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	/**
	 * 勘定科目ごとの実績値から予算比を計算してDTOを返す
	 */
	@Transactional
	public List<RatioOfBudgetDto> execute(FinancialStatement fs, UserSession userSession) {
		//予算データを取得
		UserId userId = userSession.userId();
		Map<AccountTitleId, MonthlyBudget> budgets = monthlyBudgetRepository.findAll(userId);
		
		//計算を行い、戻り値クラスに詰め替える
		List<RatioOfBudgetDto> data = new ArrayList<>();
		for (AccountTitleId each : budgets.keySet()) {
			//勘定科目ごとの予算
			MonthlyBudget monthlyBudget = budgets.get(each);
			
			//勘定科目ごとの実績値
			int actualAmount = fs.calculateTotalAmount(each);
			
			//予算に占める実績値の割合
			BigDecimal ratio = monthlyBudget.ratioOfBudget(actualAmount);
			
			data.add(new RatioOfBudgetDto(each.value(), monthlyBudget.value(), ratio));
		}
		
		return data;
	}
	
}

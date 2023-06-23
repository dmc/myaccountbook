package io.github.wtbyt298.myaccountbook.application.query.service.budget;

import java.util.List;

import io.github.wtbyt298.myaccountbook.application.query.model.budget.MonthlyBudgetDto;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 月次予算データ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchMonthlyBudgetDataQueryService {

	List<MonthlyBudgetDto> fetchAll(UserId userId);
	
}

package io.github.wtbyt298.accountbook.application.query.service.summary;

import java.time.YearMonth;

import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;
import io.github.wtbyt298.accountbook.domain.shared.types.SummaryType;

/**
 * 財務諸表のデータ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FinancialStatementQueryService {

	FinancialStatement fetch(YearMonth yearMonth, UserId userId, SummaryType summaryType);
	
}

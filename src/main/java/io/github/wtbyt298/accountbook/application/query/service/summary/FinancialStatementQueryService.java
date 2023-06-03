package io.github.wtbyt298.accountbook.application.query.service.summary;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map.Entry;

import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 財務諸表のデータ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FinancialStatementQueryService {

	List<Entry<String, BigDecimal>> aggregateByAccountTitle(YearMonth yearMonth, UserId userId, AccountingType accountingType);
	
	FinancialStatement aggregateIncludingSubAccountTitle(YearMonth yearMonth, UserId userId);
	
}

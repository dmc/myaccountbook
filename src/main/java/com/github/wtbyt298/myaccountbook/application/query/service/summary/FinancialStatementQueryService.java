package com.github.wtbyt298.myaccountbook.application.query.service.summary;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Map.Entry;

import com.github.wtbyt298.myaccountbook.application.query.model.summary.FinancialStatement;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 財務諸表のデータ取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FinancialStatementQueryService {

	List<Entry<String, BigDecimal>> aggregateByAccountTitle(YearMonth yearMonth, UserId userId, AccountingType accountingType);
	
	FinancialStatement aggregateIncludingSubAccountTitle(YearMonth yearMonth, UserId userId);
	
}

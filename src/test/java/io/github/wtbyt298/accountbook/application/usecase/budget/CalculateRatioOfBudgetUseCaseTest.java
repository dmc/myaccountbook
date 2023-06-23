package io.github.wtbyt298.accountbook.application.usecase.budget;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import io.github.wtbyt298.accountbook.application.query.model.summary.FinancialStatement;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudget;
import io.github.wtbyt298.accountbook.domain.model.budget.MonthlyBudgetRepository;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

class CalculateRatioOfBudgetUseCaseTest {

	@Mock
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Mock
	private UserSession userSession;
	
	@Mock
	private FinancialStatement fs;
	
	@InjectMocks
	private CalculateRatioOfBudgetUseCase calculateRatioOfBudgetUseCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//依存オブジェクトの設定
		when(userSession.userId()).thenReturn(UserId.valueOf("TEST_USER"));
		
		//予算額の設定
		Map<AccountTitleId, MonthlyBudget> budgets = new LinkedHashMap<>();
		budgets.put(AccountTitleId.valueOf("401"), MonthlyBudget.valueOf(50000));
		budgets.put(AccountTitleId.valueOf("402"), MonthlyBudget.valueOf(50000));
		when(monthlyBudgetRepository.findAll(userSession.userId())).thenReturn(budgets);
		
		//実績値の設定
		when(fs.calculateTotalAmount(AccountTitleId.valueOf("401"))).thenReturn(10000);
		when(fs.calculateTotalAmount(AccountTitleId.valueOf("402"))).thenReturn(20000);
	}
	
	@Test
	void 損益計算書オブジェクトとユーザセッションを渡すと予算に対する実績値の割合を計算して結果をDTOに詰めて返す() {
		//given:
		
		
		//when:テスト対象メソッドを実行する
		List<RatioOfBudgetDto> data = calculateRatioOfBudgetUseCase.execute(fs, userSession);
		
		//then:予算比が勘定科目ごとに正しく計算されている
		assertAll(
			() -> assertEquals("401", data.get(0).getAccountTitleId()),
			() -> assertEquals(50000, data.get(0).getBudgetAmount()),
			() -> assertEquals(BigDecimal.valueOf(0.2).setScale(4), data.get(0).getRatio()), //実績値10000 ÷ 予算額50000 = 0.2000
			() -> assertEquals("402", data.get(1).getAccountTitleId()),
			() -> assertEquals(50000, data.get(1).getBudgetAmount()),
			() -> assertEquals(BigDecimal.valueOf(0.4).setScale(4), data.get(1).getRatio()) //実績値20000 ÷ 予算額50000 = 0.4000
		);
	}

}

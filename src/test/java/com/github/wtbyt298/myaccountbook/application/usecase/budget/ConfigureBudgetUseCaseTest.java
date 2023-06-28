package com.github.wtbyt298.myaccountbook.application.usecase.budget;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import com.github.wtbyt298.myaccountbook.application.usecase.budget.ConfigureBudgetUseCase;
import com.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import com.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import com.github.wtbyt298.myaccountbook.domain.model.budget.MonthlyBudget;
import com.github.wtbyt298.myaccountbook.domain.model.budget.MonthlyBudgetRepository;
import com.github.wtbyt298.myaccountbook.domain.model.user.UserId;
import com.github.wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;

class ConfigureBudgetUseCaseTest {
	
	@Mock
	private MonthlyBudgetRepository monthlyBudgetRepository;
	
	@Mock
	private AccountTitleRepository accountTitleRepository;
	
	@Mock
	private UserSession userSession;
	
	@InjectMocks
	private ConfigureBudgetUseCase configureBudgetUseCase;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		//依存オブジェクトの設定
		when(userSession.userId()).thenReturn(UserId.valueOf("TEST_USER"));
	}

	@Test
	void 勘定科目IDと予算額を渡すと渡した値で予算が保存される() {
		//given:予算はまだ保存されていない
		when(monthlyBudgetRepository.exists(any(), any())).thenReturn(false);
		
		//勘定科目は費用の科目である
		AccountTitle expenses = AccountTitleTestFactory.create("401", "食費", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(any())).thenReturn(expenses);
		
		//when:勘定科目IDと予算額のMapを渡し、テスト対象メソッドを実行する
		Map<String, Integer> command = new HashMap<>();
		command.put(expenses.id().value(), 10000);
		configureBudgetUseCase.execute(command, userSession);
		
		//リポジトリのsaveメソッドに渡す値をキャプチャする
		ArgumentCaptor<MonthlyBudget> captor = ArgumentCaptor.forClass(MonthlyBudget.class);
		verify(monthlyBudgetRepository).save(captor.capture(), any(), any());
		
		//then:渡された値で予算が保存されている
		MonthlyBudget captured = captor.getValue();
		assertEquals(10000, captured.value());
	}
	
	@Test
	void 予算額が0で予算が未設定の場合は処理を実行しない() {
		//given:予算はまだ保存されていない
		when(monthlyBudgetRepository.exists(any(), any())).thenReturn(false);
		
		//勘定科目は費用の科目である
		AccountTitle expenses = AccountTitleTestFactory.create("401", "食費", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(any())).thenReturn(expenses);
		
		//when:勘定科目IDと予算額のMapを渡し、テスト対象メソッドを実行する
		Map<String, Integer> command = new HashMap<>();
		command.put(expenses.id().value(), 0); //予算額は0
		configureBudgetUseCase.execute(command, userSession);
		
		//then:リポジトリのsaveメソッドは実行されない
		verify(monthlyBudgetRepository, times(0)).save(any(), any(), any());
	}
	
	@Test
	void 予算額が0で予算が設定済みの場合は処理を実行し上書きする() {
		//given:予算は保存済みである
		when(monthlyBudgetRepository.exists(any(), any())).thenReturn(true);
		
		//勘定科目は費用の科目である
		AccountTitle expenses = AccountTitleTestFactory.create("401", "食費", AccountingType.EXPENSES);
		when(accountTitleRepository.findById(any())).thenReturn(expenses);
		
		//when:勘定科目IDと予算額のMapを渡し、テスト対象メソッドを実行する
		Map<String, Integer> command = new HashMap<>();
		command.put(expenses.id().value(), 0); //予算額は0
		configureBudgetUseCase.execute(command, userSession);
		
		//then:リポジトリのsaveメソッドが1度だけ実行される
		verify(monthlyBudgetRepository, times(1)).save(any(), any(), any());
	}
	
	@Test
	void 渡された勘定科目IDが費用の科目のものでない場合は例外発生() {
		//given:勘定科目は費用の科目ではない（ここでは資産を設定）
		AccountTitle assets = AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS);
		when(accountTitleRepository.findById(any())).thenReturn(assets);
		
		//when:勘定科目IDと予算額のMapを渡し、テスト対象メソッドを実行する
		Map<String, Integer> command = new HashMap<>();
		command.put(assets.id().value(), 100000);
		
		Exception exception = assertThrows(RuntimeException.class, () -> configureBudgetUseCase.execute(command, userSession));
		
		//then:想定した例外が発生している
		assertEquals("費用の科目以外には予算を設定できません。", exception.getMessage());
	}
}

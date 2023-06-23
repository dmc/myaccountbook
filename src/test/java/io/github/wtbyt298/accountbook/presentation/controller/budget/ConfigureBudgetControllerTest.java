package io.github.wtbyt298.accountbook.presentation.controller.budget;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import io.github.wtbyt298.accountbook.application.usecase.budget.ConfigureBudgetUseCase;
import io.github.wtbyt298.accountbook.presentation.forms.budget.ConfigureBudgetDetailForm;
import io.github.wtbyt298.accountbook.presentation.forms.budget.ConfigureBudgetForm;

@SpringBootTest
class ConfigureBudgetControllerTest {
	
	private MockMvc mockMvc;
	
	@MockBean
	private ConfigureBudgetUseCase configureBudgetUseCase;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private ConfigureBudgetController configureBudgetController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(configureBudgetController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信すると予算設定画面を表示する() throws Exception {
		//when:
		mockMvc.perform(get("/settings/budget"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("budgetForm"))
			.andExpect(view().name("/settings/budget"));
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/settings/budget"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}

	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがなければ予算設定処理を実行し予算設定画面にリダイレクトされる() throws Exception {
		//given:入力フォームは正しく入力されている
		ConfigureBudgetForm form = validConfigureBudgetForm();
		
		//when:
		mockMvc.perform(post("/budget/configure").flashAttr("budgetForm", form).with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().hasNoErrors())
			.andExpect(view().name("redirect:/settings/budget"));
		
		//then:予算設定処理が1度だけ実行されている
		verify(configureBudgetUseCase, times(1)).execute(any(), any());
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがある場合は予算設定画面を再表示する() throws Exception {
		//given:入力フォームが正しく入力されていない
		ConfigureBudgetForm form = invalidConfigureBudgetForm();
		
		//when:
		mockMvc.perform(post("/budget/configure").flashAttr("budgetForm", form).with(csrf()))
			.andExpect(model().hasErrors())
			.andExpect(model().attribute("budgetForm", form))
			.andExpect(status().isOk())
			.andExpect(view().name("/settings/budget"));
		
		//then:予算設定処理は実行されない
		verify(configureBudgetUseCase, times(0)).execute(any(), any());
	}
	
	/**
	 * バリデーションを満たしたフォームクラスを作成する
	 */
	private ConfigureBudgetForm validConfigureBudgetForm() {
		List<ConfigureBudgetDetailForm> details = new ArrayList<>();
		details.add(new ConfigureBudgetDetailForm("401", "食費", 50000));
		details.add(new ConfigureBudgetDetailForm("402", "消耗品費", 0));
		
		return new ConfigureBudgetForm(details);
	}
	
	/**
	 * バリデーション違反のあるフォームクラスを作成する
	 */
	private ConfigureBudgetForm invalidConfigureBudgetForm() {
		List<ConfigureBudgetDetailForm> details = new ArrayList<>();
		details.add(new ConfigureBudgetDetailForm("401", "食費", -50000)); //予算額がマイナスになっている
		
		return new ConfigureBudgetForm(details);
	}
	
}

package wtbyt298.myaccountbook.presentation.controller.accounttitle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.helper.testfactory.AccountTitleTestFactory;
import wtbyt298.myaccountbook.presentation.controller.accounttitle.FetchListOfAccountTitleController;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.AccountTitleViewModel;

@SpringBootTest
class FetchListOfAccountTitleControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@MockBean
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private FetchListOfAccountTitleController fetchListOfAccountTitleController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(fetchListOfAccountTitleController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信すると勘定科目一覧画面を表示する() throws Exception {
		//given:リポジトリのメソッドの戻り値の設定
		List<AccountTitle> accountTitles = new ArrayList<>();
		accountTitles.add(AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS));
		accountTitles.add(AccountTitleTestFactory.create("102", "普通預金", AccountingType.ASSETS));
		
		when(accountTitleRepository.findAll()).thenReturn(accountTitles);
		
		//when:
		MvcResult result = mockMvc.perform(get("/accounttitle/list"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("accountTitles"))
			.andExpect(view().name("/accounttitle/list"))
			.andReturn();
		
		//then:ビューモデルへの詰め替えが正しく実行されている
		@SuppressWarnings("unchecked")
		List<AccountTitleViewModel> viewModels = (List<AccountTitleViewModel>) result.getModelAndView().getModel().get("accountTitles");
		assertAll(
			() -> assertEquals(viewModels.get(0).getId(), accountTitles.get(0).id().value()),
			() -> assertEquals(viewModels.get(1).getId(), accountTitles.get(1).id().value())
		);
		
		verify(accountTitleRepository, times(1)).findAll();
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/accounttitle/list"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}

}

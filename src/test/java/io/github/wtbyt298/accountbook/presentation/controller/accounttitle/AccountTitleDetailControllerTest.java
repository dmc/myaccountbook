package io.github.wtbyt298.accountbook.presentation.controller.accounttitle;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.helper.testfactory.AccountTitleTestFactory;

@SpringBootTest
class AccountTitleDetailControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@InjectMocks
	@Autowired
	private AccountTitleDetailController accountTitleDetailController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
		mockMvc = MockMvcBuilders.standaloneSetup(accountTitleDetailController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信すると勘定科目詳細画面を表示する() throws Exception {
		//given:リポジトリのfindByIdメソッドは戻り値を返す
		AccountTitleId id = AccountTitleId.valueOf("101");
		when(accountTitleRepository.findById(id)).thenReturn(AccountTitleTestFactory.create("101", "現金", AccountingType.ASSETS));
		
		//when:
		mockMvc.perform(get("/accounttitle/detail/101"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("empty"))
			.andExpect(view().name("/accounttitle/detail"));
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/accounttitle/detail"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}

}

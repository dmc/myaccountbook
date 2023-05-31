package io.github.wtbyt298.accountbook.presentation.controller.shared;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import io.github.wtbyt298.accountbook.presentation.controller.user.SignUpController;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.SpringSecurityUserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.viewmodels.accounttitle.MergedAccountTitleViewModel;

@SpringBootTest
class ModelAttributeUtilityControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private UserSessionProvider userSessionProvider;
	
	@MockBean
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;
	
	@InjectMocks
	@Autowired
	private ModelAttributeUtilityController modelAttributeUtilityController;
	
	@Autowired
	private SignUpController signUpController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
		mockMvc = MockMvcBuilders.standaloneSetup(signUpController)
			.setViewResolvers(viewResolver)
			.setControllerAdvice(modelAttributeUtilityController)
			.build();
	}
	
	@Test
	@WithMockUser("TEST_USER")
	void Modelの属性にログイン中のユーザ名が追加される() throws Exception {
		//given:依存オブジェクトは戻り値を返す
		when(userSessionProvider.getUserSession()).thenReturn(new SpringSecurityUserSessionProvider().getUserSession());
		
		//when:
		mockMvc.perform(get("/user/signup"))
			.andExpect(model().attribute("loginUser", "TEST_USER"));
	}
	
	@Test
	@WithMockUser("TEST_USER")
	@SuppressWarnings("unchecked")
	void Modelの属性に勘定科目の一覧が追加される() throws Exception {
		//given:依存オブジェクトは戻り値を返す
		when(userSessionProvider.getUserSession()).thenReturn(new SpringSecurityUserSessionProvider().getUserSession());
		List<AccountTitleAndSubAccountTitleDto> data = new ArrayList<>();
		data.add(new AccountTitleAndSubAccountTitleDto("101", "現金", "0", ""));
		data.add(new AccountTitleAndSubAccountTitleDto("401", "食費", "0", "その他"));
		when(fetchListQueryService.fetchAll(any())).thenReturn(data);
		
		//when:
		MvcResult result = mockMvc.perform(get("/user/signup"))
			.andExpect(model().attributeExists("selectBoxElements"))
			.andReturn();
		List<MergedAccountTitleViewModel> viewModels = (List<MergedAccountTitleViewModel>) result.getModelAndView().getModel().get("selectBoxElements");
		
		//then:データ詰め替え処理が正しく実行されている
		assertAll(
			() -> assertEquals(data.size(), viewModels.size()),
			() -> assertEquals("101-0", viewModels.get(0).getMergedId()),
			() -> assertEquals("現金", viewModels.get(0).getMergedName()),
			() -> assertEquals("401-0", viewModels.get(1).getMergedId()),
			() -> assertEquals("食費：その他", viewModels.get(1).getMergedName())
		);
	}
	
}

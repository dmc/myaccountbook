package com.github.wtbyt298.myaccountbook.presentation.controller.subaccounttitle;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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

import com.github.wtbyt298.myaccountbook.application.usecase.subaccounttitle.AppendSubAccountTitleUseCase;
import com.github.wtbyt298.myaccountbook.presentation.controller.subaccounttitle.AppendSubAccountTitleController;
import com.github.wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;

@SpringBootTest
class AppendSubAccountTitleControllerTest {
	
	private MockMvc mockMvc;

	@MockBean
	private AppendSubAccountTitleUseCase appendSubAccountTitleUseCase;
	
	@MockBean
	private UserSessionProvider userSessionProvider;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private AppendSubAccountTitleController appendSubAccountTitleController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(appendSubAccountTitleController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信すると補助科目追加処理が実行される() throws Exception {
		//when:
		mockMvc.perform(post("/accounttitle/append").with(csrf())
			.param("parentId", "102")
			.param("subAccountTitleName", "第四北越銀行"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/accounttitle/detail/102"));
		
		//then:補助科目追加処理が1度だけ実行される
		verify(appendSubAccountTitleUseCase, times(1)).execute(any(), any());
	}

}

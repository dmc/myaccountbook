package wtbyt298.myaccountbook.presentation.controller.subaccounttitle;

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

import wtbyt298.myaccountbook.application.usecase.subaccounttitle.RenameSubAccountTitleUseCase;
import wtbyt298.myaccountbook.presentation.controller.subaccounttitle.RenameSubAccountTitleController;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;

@SpringBootTest
class RenameSubAccountTitleControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private RenameSubAccountTitleUseCase renameSubAccountTitleUseCase;
	
	@MockBean
	private UserSessionProvider userSessionProvider;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private RenameSubAccountTitleController renameSubAccountTitleController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(renameSubAccountTitleController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信すると補助科目名変更処理が実行される() throws Exception {
		//when:
		mockMvc.perform(post("/accounttitle/edit").with(csrf())
			.param("parentId", "101")
			.param("subId", "0")
			.param("newName", "その他"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/accounttitle/detail/101"));
		
		//then:補助科目名変更処理が1度だけ実行されている
		verify(renameSubAccountTitleUseCase, times(1)).execute(any(), any());
	}

}

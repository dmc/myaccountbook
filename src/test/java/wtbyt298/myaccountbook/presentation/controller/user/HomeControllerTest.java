package wtbyt298.myaccountbook.presentation.controller.user;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import wtbyt298.myaccountbook.presentation.controller.user.HomeController;

@SpringBootTest
class HomeControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private HomeController homeController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(homeController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信するとホーム画面を表示する() throws Exception {
		//when:
		mockMvc.perform(get("/user/home"))
			.andExpect(status().isOk())
			.andExpect(view().name("/user/home"));
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/user/home"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}

}

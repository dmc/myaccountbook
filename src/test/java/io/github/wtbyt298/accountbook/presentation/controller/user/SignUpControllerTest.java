package io.github.wtbyt298.accountbook.presentation.controller.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import io.github.wtbyt298.accountbook.application.shared.exception.UseCaseException;
import io.github.wtbyt298.accountbook.application.usecase.user.CreateUserUseCase;
import io.github.wtbyt298.accountbook.presentation.params.user.CreateUserParam;

@SpringBootTest
class SignUpControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private CreateUserUseCase createUserUseCase;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private SignUpController signUpController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
		mockMvc = MockMvcBuilders.standaloneSetup(signUpController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	void GETリクエストを送信するとユーザ登録画面を表示する() throws Exception {
		//when:
		mockMvc.perform(get("/user/signup"))
			.andExpect(status().isOk())
			.andExpect(view().name("/user/signup"));
	}
	
	@Test
	void POSTリクエストを送信してバリデーションエラーがなければユーザ登録を行いホーム画面へリダイレクトされる() throws Exception {
		//given:フォームの内容が正しく入力されている
		CreateUserParam param = validUserParam();
		
		//when:
		mockMvc.perform(post("/signup").flashAttr("userParam", param).with(csrf()))
			.andExpect(model().hasNoErrors())
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/user/home"));
		
		//then:ユーザ登録処理が1度だけ実行される
		verify(createUserUseCase, times(1)).execute(any());
	}
	
	@Test
	void POSTリクエストを送信してバリデーションエラーがある場合ユーザ登録画面を再表示する() throws Exception {
		//given:ユーザIDが空白である
		CreateUserParam param = new CreateUserParam();
		param.setId("");
		
		//when:
		mockMvc.perform(post("/signup").flashAttr("userParam", param).with(csrf()))
			.andExpect(model().hasErrors())
			.andExpect(model().attribute("userParam", param))
			.andExpect(status().isOk())
			.andExpect(view().name("/user/signup"));
	}
	
	@Test
	void POSTリクエストを送信してUseCaseExceptionが発生した場合ユーザ登録画面を再表示する() throws Exception {
		//given:フォームの内容が正しく入力されている
		CreateUserParam param = validUserParam();
		//given:ユーザ登録処理を実行すると例外が発生する
		doThrow(new UseCaseException("同一IDのユーザが既に存在しています。")).when(createUserUseCase).execute(any());
				
		//when:
		mockMvc.perform(post("/signup").flashAttr("userParam", param).with(csrf()))
			.andExpect(model().hasNoErrors())
			.andExpect(model().attribute("errorMessage", "同一IDのユーザが既に存在しています。"))
			.andExpect(status().isOk())
			.andExpect(view().name("/user/signup"));
		
		//then:ユーザ登録処理が1度だけ実行される
		verify(createUserUseCase, times(1)).execute(any());
	}
	
	/**
	 * バリデーションを満たしたユーザ登録パラメータを生成する
	 */
	private CreateUserParam validUserParam() {
		CreateUserParam param = new CreateUserParam();
		param.setId("TEST_USER");
		param.setPassword("testpassword0123");
		param.setMailAddress("test@example.com");
		return param;
	}

}

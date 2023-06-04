package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import java.time.LocalDate;
import java.util.ArrayList;
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
import io.github.wtbyt298.accountbook.application.usecase.journalentry.RegisterJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.shared.exception.CannotCreateJournalEntryException;
import io.github.wtbyt298.accountbook.presentation.forms.journalentry.RegisterJournalEntryForm;

@SpringBootTest
class RegisterJournalEntryControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private RegisterJournalEntryUseCase registerJournalEntryUseCase;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private RegisterJournalEntryController registerJournalEntryController;
	
	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
        mockMvc = MockMvcBuilders.standaloneSetup(registerJournalEntryController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信すると仕訳登録画面を表示する() throws Exception {
		//when:
		mockMvc.perform(get("/entry/register"))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/entry/register"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがなければ仕訳登録処理を行い仕訳登録画面にリダイレクトされる() throws Exception {
		//given:入力フォームは正しく入力されている
		RegisterJournalEntryForm form = validRegisterJournalEntryForm();
		
		//when:
		mockMvc.perform(post("/entry/register").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().hasNoErrors())
			.andExpect(model().attributeDoesNotExist("errorMessage"))
			.andExpect(view().name("redirect:/entry/register"));
		
		//then:仕訳登録処理が1度だけ実行されている
		verify(registerJournalEntryUseCase, times(1)).execute(any(), any());
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがある場合は仕訳登録画面を再表示する() throws Exception {
		//given:入力フォームが正しく入力されていない
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		
		//when:
		mockMvc.perform(post("/entry/register").flashAttr("entryForm", form).with(csrf()))
			.andExpect(model().hasErrors())
			.andExpect(model().attribute("entryForm", form))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
		
		//then:仕訳登録処理は実行されない
		verify(registerJournalEntryUseCase, times(0)).execute(any(), any());
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してCannotCreateJournalEntryExceptionが発生した場合は仕訳登録画面を再表示する() throws Exception {
		//given:仕訳登録処理を実行するとCannotCreateJournalEntryExceptionが発生する
		Exception exception = new CannotCreateJournalEntryException("");
		doThrow(exception).when(registerJournalEntryUseCase).execute(any(), any());
		
		//入力フォームは正しく入力されている
		RegisterJournalEntryForm form = validRegisterJournalEntryForm();
		
		//when:
		mockMvc.perform(post("/entry/register").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().hasNoErrors())
			.andExpect(model().attribute("entryForm", form))
			.andExpect(model().attribute("errorMessage", exception.getMessage()))
			.andExpect(view().name("/entry/register"));
	}

	/**
	 * バリデーションを満たしたフォームクラスを作成する
	 */
	private RegisterJournalEntryForm validRegisterJournalEntryForm() {
		return new RegisterJournalEntryForm(
			LocalDate.now(), 
			"コントローラのテスト", 
			new ArrayList<>(), 
			new ArrayList<>());
	}
	
}

package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.time.LocalDate;
import java.time.YearMonth;
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
import io.github.wtbyt298.accountbook.application.usecase.journalentry.CorrectJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.shared.exception.CannotCreateJournalEntryException;
import io.github.wtbyt298.accountbook.presentation.forms.journalentry.RegisterJournalEntryForm;

@SpringBootTest
class CorrectJournalEntryControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private CorrectJournalEntryUseCase correctJournalEntryUseCase;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private CorrectJournalEntryController correctJournalEntryController;
	
	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        mockMvc = MockMvcBuilders.standaloneSetup(correctJournalEntryController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがなければ仕訳訂正処理を実行し仕訳一覧画面にリダイレクトされる() throws Exception {
		//given:入力フォームは正しく入力されている
		RegisterJournalEntryForm form = validRegisterJournalEntryForm();
		
		//when:
		YearMonth yearMonth = YearMonth.now();
		mockMvc.perform(post("/entry/correct").flashAttr("entryForm", form).param("entryId", "TEST_ID").with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().hasNoErrors())
			.andExpect(model().attributeDoesNotExist("errorMessage"))
			.andExpect(view().name("redirect:/entry/entries/" + yearMonth.toString())); //現在の年月のページが表示される
		
		//then:仕訳訂正処理が1度だけ実行されている
		verify(correctJournalEntryUseCase, times(1)).execute(any(), any(), any());
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してバリデーションエラーがあった場合は仕訳詳細画面を再表示する() throws Exception {
		//given:入力フォームが正しく入力されていない
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		
		//when:
		
		mockMvc.perform(post("/entry/correct").flashAttr("entryForm", form).with(csrf()))
			.andExpect(model().hasErrors())
			.andExpect(model().attribute("entryForm", form))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/entry"));
		
		//then:仕訳訂正処理は実行されない
		verify(correctJournalEntryUseCase, times(0)).execute(any(), any(), any());
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信してCannotCreateJournalEntryExceptionが発生した場合はエラーメッセージを表示する() throws Exception {
		//given:仕訳訂正処理を実行するとCannotCreateJournalEntryExceptionが発生する
		Exception exception = new CannotCreateJournalEntryException("");
		doThrow(exception).when(correctJournalEntryUseCase).execute(any(), any(), any());
		//入力フォームは正しく入力されている
		RegisterJournalEntryForm form = validRegisterJournalEntryForm();
		
		//when:
		mockMvc.perform(post("/entry/correct").flashAttr("entryForm", form).param("entryId", "TEST_ID").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().hasNoErrors())
			.andExpect(model().attribute("entryForm", form))
			.andExpect(model().attribute("errorMessage", exception.getMessage()))
			.andExpect(view().name("/entry/entry"));
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

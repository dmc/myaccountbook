package io.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
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
import io.github.wtbyt298.myaccountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.myaccountbook.infrastructure.shared.exception.RecordNotFoundException;

@SpringBootTest
class FetchListOfJournalEntryControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private FetchJournalEntryDataQueryService fetchJournalEntryDataQueryService;
	
	@MockBean
	private UserSession userSession;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private FetchListOfJournalEntryController fetchListOfJournalEntryController;
	
	@BeforeEach
	void setUp() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
        mockMvc = MockMvcBuilders.standaloneSetup(fetchListOfJournalEntryController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void 年月を指定してGETリクエストを送信すると仕訳一覧画面を表示する() throws Exception {
		//when:
		mockMvc.perform(get("/entry/entries/2023-05").param("selectedYearMonth", "2023-05"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("entries"))
			.andExpect(model().attributeDoesNotExist("errorMessage"))
			.andExpect(view().name("/entry/entries"));
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信してRecordNotFoundExceptionが発生した場合はエラーメッセージを表示する() throws Exception {
		//given:クエリサービスのfetchAllメソッドを実行するとRecordNotFoundExceptionが発生する
		Exception exception = new RecordNotFoundException("");
		doThrow(exception).when(fetchJournalEntryDataQueryService).fetchAll(any(), any(), any());
		
		//when:
		mockMvc.perform(get("/entry/entries/2023-05").param("selectedYearMonth", "2023-05"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("errorMessage", exception.getMessage()))
			.andExpect(view().name("/entry/entries"));
	}
	
	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/entry/entries/2023-05"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}

}

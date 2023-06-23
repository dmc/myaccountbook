package io.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import io.github.wtbyt298.myaccountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.myaccountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import io.github.wtbyt298.myaccountbook.domain.model.journalentry.EntryId;

@SpringBootTest
class FetchJournalEntryControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private FetchJournalEntryDataQueryService fetchJournalEntryDataQueryService;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private FetchJournalEntryController fetchJournalEntryController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(fetchJournalEntryController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void GETリクエストを送信すると仕訳編集画面を表示する() throws Exception {
		//given:クエリサービスのfetchOneメソッドは戻り値を返す
		EntryId entryId = EntryId.fromString("TEST_ID");
		JournalEntryDto dto = new JournalEntryDto(entryId.value(), LocalDate.now(), "テストです。", 1000, new ArrayList<>());
		when(fetchJournalEntryDataQueryService.fetchOne(entryId)).thenReturn(dto);
		
		//when:
		mockMvc.perform(get("/entry/edit/TEST_ID"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("entryForm"))
			.andExpect(model().attribute("entryId", entryId.value()))
			.andExpect(view().name("/entry/edit"));
		
		//then:仕訳取得処理が1度だけ実行されている
		verify(fetchJournalEntryDataQueryService, times(1)).fetchOne(entryId);
	}

	@Test
	void 権限なしでアクセスするとログイン画面にリダイレクトされる() throws Exception {
		//when:
		mockMvc.perform(get("/entry/entry/TEST_ID"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("**/user/login"));
	}
	
}

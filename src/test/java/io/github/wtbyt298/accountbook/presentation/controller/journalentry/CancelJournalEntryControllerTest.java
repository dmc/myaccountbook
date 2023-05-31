package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.time.YearMonth;
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
import io.github.wtbyt298.accountbook.application.usecase.journalentry.CancelJournalEntryUseCase;
import io.github.wtbyt298.accountbook.domain.model.journalentry.EntryId;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

@SpringBootTest
class CancelJournalEntryControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private CancelJournalEntryUseCase cancelJournalEntryUseCase;
	
	@MockBean
	private UserSessionProvider userSessionProvider;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@InjectMocks
	@Autowired
	private CancelJournalEntryController cancelJournalEntryController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
		mockMvc = MockMvcBuilders.standaloneSetup(cancelJournalEntryController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void POSTリクエストを送信すると仕訳取消処理が実行される() throws Exception {
		//given:コントローラ内で使用するオブジェクトを生成
		EntryId entryId = EntryId.fromString("TEST_ID");
		//リダイレクト先のURL
		//現在の年月のページにリダイレクトされる（例：/entry/entries/2023-05）
		final String currentYearMonth = YearMonth.now().toString();
		final String redirectPath = "redirect:/entry/entries/" + currentYearMonth;
		
		//when:
		mockMvc.perform(post("/entry/correct").with(csrf())
			.param("cancel", "cancel")
			.param("entryId", entryId.value()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(redirectPath));
		
		//then:仕訳取消処理が1度だけ実行されている
		verify(cancelJournalEntryUseCase, times(1)).execute(entryId, userSessionProvider.getUserSession());
	}

}

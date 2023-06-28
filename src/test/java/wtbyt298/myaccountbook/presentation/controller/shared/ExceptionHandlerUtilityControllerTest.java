package wtbyt298.myaccountbook.presentation.controller.shared;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import wtbyt298.myaccountbook.application.query.service.journalentry.FetchJournalEntryDataQueryService;
import wtbyt298.myaccountbook.application.shared.exception.UseCaseException;
import wtbyt298.myaccountbook.domain.shared.exception.DomainException;
import wtbyt298.myaccountbook.infrastructure.shared.exception.RecordNotFoundException;
import wtbyt298.myaccountbook.presentation.controller.journalentry.FetchJournalEntryController;
import wtbyt298.myaccountbook.presentation.controller.shared.ExceptionHandlerUtilityController;

@SpringBootTest
@Transactional
class ExceptionHandlerUtilityControllerTest {

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
			.setControllerAdvice(new ExceptionHandlerUtilityController())
			.build();
		
		
	}
	
	@Test
	@WithMockUser
	void DomainExceptionが発生すると400_BadRequestとして処理される() throws Exception {
		//given:クエリサービスのメソッドを実行するとDomainExceptionがスローされる
		Exception exception = new DomainException("");
		when(fetchJournalEntryDataQueryService.fetchOne(any())).thenThrow(exception);
		
		//when:
		mockMvc.perform(get("/entry/edit/001"))
			.andExpect(status().isBadRequest())
			.andExpect(model().attribute("status", 400))
			.andExpect(model().attribute("error", "BAD_REQUEST"))
			.andExpect(model().attribute("message", exception.getMessage()))
			.andExpect(view().name("error"));
	}

	@Test
	@WithMockUser
	void UseCaseExceptionが発生すると400_BAD_REQUESTとして処理される() throws Exception {
		//given:クエリサービスのメソッドを実行するとUseCaseExceptionがスローされる
		Exception exception = new UseCaseException("");
		when(fetchJournalEntryDataQueryService.fetchOne(any())).thenThrow(exception);
		
		//when:
		mockMvc.perform(get("/entry/edit/001"))
			.andExpect(status().isBadRequest())
			.andExpect(model().attribute("status", 400))
			.andExpect(model().attribute("error", "BAD_REQUEST"))
			.andExpect(model().attribute("message", exception.getMessage()))
			.andExpect(view().name("error"));
	}
	
	@Test
	@WithMockUser
	void RecordNotFoundExceptionが発生すると404_NOT_FOUNDとして処理される() throws Exception {
		//given:クエリサービスのメソッドを実行するとRecordNotFoundExceptionがスローされる
		Exception exception = new RecordNotFoundException("");
		when(fetchJournalEntryDataQueryService.fetchOne(any())).thenThrow(exception);
		
		//when:
		mockMvc.perform(get("/entry/edit/001"))
			.andExpect(status().isNotFound())
			.andExpect(model().attribute("status", 404))
			.andExpect(model().attribute("error", "NOT_FOUND"))
			.andExpect(model().attribute("message", exception.getMessage()))
			.andExpect(view().name("error"));
	}
	
	@Test
	@WithMockUser
	void その他の例外が発生すると500_INTERNAL_SERVER_ERRORとして処理される() throws Exception {
		//given:クエリサービスのメソッドを実行するとRuntimeExceptionがスローされる
		Exception exception = new RuntimeException("");
		when(fetchJournalEntryDataQueryService.fetchOne(any())).thenThrow(exception);
		
		//when:
		mockMvc.perform(get("/entry/edit/001"))
			.andExpect(status().isInternalServerError())
			.andExpect(model().attribute("status", 500))
			.andExpect(model().attribute("error", "INTERNAL_SERVER_ERROR"))
			.andExpect(model().attribute("message", "予期せぬエラーが発生しました。"))
			.andExpect(view().name("error"));
	}
	
}

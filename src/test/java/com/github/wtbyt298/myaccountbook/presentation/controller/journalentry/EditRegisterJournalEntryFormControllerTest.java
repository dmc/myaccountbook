package com.github.wtbyt298.myaccountbook.presentation.controller.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

import com.github.wtbyt298.myaccountbook.presentation.controller.journalentry.EditRegisterJournalEntryFormController;
import com.github.wtbyt298.myaccountbook.presentation.forms.journalentry.RegisterJournalEntryForm;

@SpringBootTest
class EditRegisterJournalEntryFormControllerTest {

	private MockMvc mockMvc;
	
	@Autowired
	private FilterChainProxy springSecurityFilterChain;
	
	@Autowired
	private EditRegisterJournalEntryFormController editRegisterJournalEntryFormController;
	
	@BeforeEach
	void setUp() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        
		mockMvc = MockMvcBuilders.standaloneSetup(editRegisterJournalEntryFormController)
			.apply(springSecurity(springSecurityFilterChain))
			.setViewResolvers(viewResolver)
			.build();
	}
	
	@Test
	@WithMockUser
	void リクエスト元のURLに応じて表示するページが切り替わる() throws Exception {
		//given:フォームクラスが初期化されている
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		form.initList();
		
		//when:POSTのURLに応じて返す画面が切り替わっている
		mockMvc.perform(post("/entry/register").param("add", "DEBIT").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
		
		mockMvc.perform(post("/entry/correct").param("add", "DEBIT").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/edit"));
	}
	
	@Test
	@WithMockUser
	void パラメータにaddを指定してPOSTリクエストを送信すると入力フォームが追加される() throws Exception {
		//given:フォームクラスが初期化されている
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		form.initList();
		
		//要素数は1である
		assertEquals(1, form.getDebitForms().size());
		
		//when:
		mockMvc.perform(post("/entry/register").param("add", "DEBIT").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
		
		//then:要素数が2に増えている
		assertEquals(2, form.getDebitForms().size());
	}
	
	@Test
	@WithMockUser
	void 入力フォームが最大数まで追加されている場合はエラーメッセージを表示する() throws Exception {
		//given:フォームが最大数まで追加されている
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		form.initList();
		for (int i = 0; i < 10; i++) {
			form.addList("DEBIT");
		}
		assertEquals(10, form.getDebitForms().size());
		
		//when:
		mockMvc.perform(post("/entry/register").param("add", "DEBIT").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attribute("error_DEBIT", "これ以上追加できません。"))
			.andExpect(view().name("/entry/register"));
		
		//then:要素数は10のままである
		assertEquals(10, form.getDebitForms().size());
	}
	
	@Test
	@WithMockUser
	void パラメータにremoveを指定してPOSTリクエストを送信すると入力フォームが削除される() throws Exception {
		//given:フォームクラスが初期化されている
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		form.initList();
		form.addList("DEBIT");
		
		//要素数は2である
		assertEquals(2, form.getDebitForms().size());
		
		//when:
		mockMvc.perform(post("/entry/register").param("remove", "DEBIT-1").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
		
		//then:要素数が1に減っている
		assertEquals(1, form.getDebitForms().size());
	}
	
	@Test
	@WithMockUser
	void 入力フォームが1つの時は削除されない() throws Exception {
		//given:フォームクラスが初期化されている
		RegisterJournalEntryForm form = new RegisterJournalEntryForm();
		form.initList();
		
		//要素数は1である
		assertEquals(1, form.getDebitForms().size());
		
		//when:
		mockMvc.perform(post("/entry/register").param("remove", "DEBIT-0").flashAttr("entryForm", form).with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name("/entry/register"));
		
		//then:要素数は1のままである
		assertEquals(1, form.getDebitForms().size());
	}

}

package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleIdAndNameDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.IAccountTitleIdAndNameQueryService;
import io.github.wtbyt298.accountbook.application.usecase.journalentry.JournalEntryRegisterUseCase;
import io.github.wtbyt298.accountbook.presentation.httprequest.journalentry.JournalEntryRegisterParam;
import jakarta.validation.Valid;

/**
 * 仕訳登録処理のコントローラ
 */
@Controller
public class JournalEntryRegisterController {
	
	@Autowired
	private IAccountTitleIdAndNameQueryService accountTitleListQueryService;
	
	@Autowired
	private JournalEntryRegisterUseCase registerUseCase;
	
	@GetMapping("/inputform")
	public String initialize(@ModelAttribute JournalEntryRegisterParam param, Model model) {
		//セレクトボックスで選択する項目を取得
		List<AccountTitleIdAndNameDto> accountTitleList = accountTitleListQueryService.findAll();
		model.addAttribute("accountTitleList", accountTitleList);
		return "inputform";
	}

	@PostMapping("/completed")
	public String register(@Valid @ModelAttribute JournalEntryRegisterParam param, BindingResult result, Model model) {
		if (result.hasErrors()) {
			//TODO セレクトボックスの入力内容を保存できるようにしたい。
			return initialize(param, model);
		}
		registerUseCase.register(param);
		return "completed";
	}

}

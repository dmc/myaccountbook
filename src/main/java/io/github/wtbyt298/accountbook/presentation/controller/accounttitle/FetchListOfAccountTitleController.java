package io.github.wtbyt298.accountbook.presentation.controller.accounttitle;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.presentation.viewmodels.accounttitle.AccountTitleViewModel;

/**
 * 勘定科目一覧取得処理のコントローラクラス
 */
@Controller
public class FetchListOfAccountTitleController {

	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	/**
	 * 勘定科目一覧画面を表示する
	 */
	@GetMapping("/accounttitle/list")
	public String load() {
		return "/accounttitle/list";
	}
	
	/**
	 * 勘定科目の表示用のデータを取得する
	 */
	@ModelAttribute("accountTitles")
	public List<AccountTitleViewModel> accountTitles() {
		List<AccountTitle> entities = accountTitleRepository.findAll();
		List<AccountTitleViewModel> viewModels = new ArrayList<>();
		for (AccountTitle each : entities) {
			viewModels.add(new AccountTitleViewModel(
				each.id().value(), 
				each.name().value(), 
				each.accountingType().lavel(), 
				each.accountingType().loanType().label(), 
				each.accountingType().summaryType().label()
			));
		}
		return viewModels;
	}
	
}

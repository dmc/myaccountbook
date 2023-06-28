package wtbyt298.myaccountbook.presentation.controller.accounttitle;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.AccountTitleViewModel;

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
		return entities.stream()
			.map(each -> mapEntityToViewModel(each))
			.toList();
	}
	
	/**
	 * エンティティをビューモデルに詰め替える
	 */
	private AccountTitleViewModel mapEntityToViewModel(AccountTitle entity) {
		return new AccountTitleViewModel(
			entity.id().value(), 
			entity.name().value(), 
			entity.accountingType().lavel(), 
			entity.accountingType().loanType().label(), 
			entity.accountingType().summaryType().label()
		);
	}
	
}

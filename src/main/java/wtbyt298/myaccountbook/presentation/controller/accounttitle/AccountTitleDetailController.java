package wtbyt298.myaccountbook.presentation.controller.accounttitle;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.AccountTitleViewModel;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.SubAccountTitleViewModel;

/**
 * 勘定科目詳細画面のコントローラクラス
 */
@Controller
public class AccountTitleDetailController {
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Autowired
	private UserSessionProvider userSessionProvider;

	/**
	 * 勘定科目詳細画面を表示する
	 */
	@GetMapping("/accounttitle/detail/{id}")
	public String load(@PathVariable String id, Model model) {
		//勘定科目の表示用データを取得する
		AccountTitleId parentId = AccountTitleId.valueOf(id);
		AccountTitleViewModel parentViewModel = createAccountTitleViewModel(parentId);
		model.addAttribute("accountTitle", parentViewModel);
		
		//補助科目の表示用データを取得する
		List<SubAccountTitleViewModel> subViewModels = createSubAccountTitleViewModels(parentId);
		model.addAttribute("subAccountTitles", subViewModels);
		
		//補助科目が作成されていない場合は補助科目の表示はせず、メッセージを表示する
		if (subViewModels.isEmpty()) {
			model.addAttribute("empty", "補助科目はまだ作成されていません。");
		}
		
		return "/accounttitle/detail";
	}
	
	/**
	 * 勘定科目のビューモデルを作成する
	 */
	private AccountTitleViewModel createAccountTitleViewModel(AccountTitleId parentId) {
		AccountTitle accountTitle = accountTitleRepository.findById(parentId);
		return mapEntityToViewModel(accountTitle);
	}
	
	/**
	 * エンティティをビューモデルに詰め替える（勘定科目）
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
	
	/**
	 * 補助科目のビューモデルのリストを取得する
	 */
	public List<SubAccountTitleViewModel> createSubAccountTitleViewModels(AccountTitleId parentId) {
		UserSession userSession = userSessionProvider.getUserSession();
		SubAccountTitles subAccountTitles = subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId());
		
		return subAccountTitles.elements().values().stream()
			.map(each -> mapEntityToViewModel(each))
			.toList();
	}
	
	/**
	 * エンティティをビューモデルに詰め替える（補助科目）
	 */
	private SubAccountTitleViewModel mapEntityToViewModel(SubAccountTitle entity) {
		return new SubAccountTitleViewModel(entity.id().value(), entity.name().value());
	}
	
}

package io.github.wtbyt298.accountbook.presentation.controller.accounttitle;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.viewmodels.accounttitle.AccountTitleViewModel;
import io.github.wtbyt298.accountbook.presentation.viewmodels.accounttitle.SubAccountTitleViewModel;

/**
 * 勘定科目詳細画面のコントローラクラス
 */
@Controller
public class AccountTitleDetailController {
	
	//例外的にコントローラからリポジトリへの参照を許容している
	
	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Autowired
	private UserSessionProvider userSessionProvider;

	@GetMapping("/accounttitle/detail/{id}")
	public String load(@PathVariable String id, Model model) {
		AccountTitleId parentId = AccountTitleId.valueOf(id);
		AccountTitleViewModel parentViewModel = createAccountTitleViewModel(parentId);
		model.addAttribute("accountTitle", parentViewModel);
		List<SubAccountTitleViewModel> subViewModels = createSubAccountTitleViewModels(parentId);
		model.addAttribute("subAccountTitles", subViewModels);
		if (subViewModels.isEmpty()) {
			model.addAttribute("empty", "補助科目はまだ作成されていません。");
		}
		return "/accounttitle/detail";
	}
	
	/**
	 * 勘定科目のビューモデルを取得する
	 */
	private AccountTitleViewModel createAccountTitleViewModel(AccountTitleId parentId) {
		AccountTitle accountTitle = accountTitleRepository.findById(parentId);
		return new AccountTitleViewModel(
			accountTitle.id().value(), 
			accountTitle.name().value(), 
			accountTitle.accountingType().lavel(), 
			accountTitle.accountingType().loanType().label(), 
			accountTitle.accountingType().summaryType().label()
		);
	}
	
	/**
	 * 補助科目名のリストを取得する
	 */
	public List<SubAccountTitleViewModel> createSubAccountTitleViewModels(AccountTitleId parentId) {
		UserSession userSession = userSessionProvider.getUserSession();
		SubAccountTitles subAccountTitles = subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId());
		List<SubAccountTitleViewModel> subNames = new ArrayList<>();
		for (SubAccountTitle each : subAccountTitles.elements().values()) {
			subNames.add(new SubAccountTitleViewModel(each.id().value(), each.name().value()));
		}
		return subNames;
	}
	
}

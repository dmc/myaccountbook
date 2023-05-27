package io.github.wtbyt298.accountbook.presentation.controller.shared;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.accountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;
import io.github.wtbyt298.accountbook.presentation.viewmodels.accounttitle.MergedAccountTitleViewModel;

/**
 * 全ての画面で共有するModelAttributeを生成するコントローラクラス
 */
@ControllerAdvice
public class ModelAttributeUtilityController {

	@Autowired
	private UserSessionProvider userSessionProvider;
	
	@Autowired
	private FetchListOfAccountTitleAndSubAccountTitleQueryService fetchListQueryService;
	
	/**
	 * ログイン中のユーザ名を返す
	 */
	@ModelAttribute("loginUser")
	public String loginUser() {
		UserSession userSession = userSessionProvider.getUserSession();
		return userSession.userId().toString();
	}
	
	/**
	 * 勘定科目のセレクトボックスに表示するデータを取得する
	 */
	@ModelAttribute("selectBoxElements")
	public List<MergedAccountTitleViewModel> selectBoxElements() {
		UserSession userSession = userSessionProvider.getUserSession();
		List<AccountTitleAndSubAccountTitleDto> data = fetchListQueryService.fetchAll(userSession.userId());
		return mapDtoToViewModel(data);
	}
	
	/**
	 * DBから取得した勘定科目データをViewモデルに詰め替える
	 */
	private List<MergedAccountTitleViewModel> mapDtoToViewModel(List<AccountTitleAndSubAccountTitleDto> data) {
		List<MergedAccountTitleViewModel> viewModels = new ArrayList<>();
		for (AccountTitleAndSubAccountTitleDto dto : data) {
			viewModels.add(new MergedAccountTitleViewModel(dto));
		}
		return viewModels;
	}
	
}

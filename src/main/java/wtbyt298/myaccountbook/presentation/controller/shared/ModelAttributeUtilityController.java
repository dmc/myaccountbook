package wtbyt298.myaccountbook.presentation.controller.shared;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import wtbyt298.myaccountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import wtbyt298.myaccountbook.application.query.service.accounttitle.FetchListOfAccountTitleAndSubAccountTitleQueryService;
import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;
import wtbyt298.myaccountbook.presentation.viewmodels.accounttitle.MergedAccountTitleViewModel;

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
	 * DTOをビューモデルに詰め替える
	 */
	private List<MergedAccountTitleViewModel> mapDtoToViewModel(List<AccountTitleAndSubAccountTitleDto> data) {
		return data.stream()
			.map(each -> new MergedAccountTitleViewModel(each))
			.toList();
	}
	
}

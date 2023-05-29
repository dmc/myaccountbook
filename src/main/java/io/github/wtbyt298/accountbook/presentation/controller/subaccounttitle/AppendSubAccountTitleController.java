package io.github.wtbyt298.accountbook.presentation.controller.subaccounttitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.application.usecase.subaccounttitle.AppendSubAccountTitleCommand;
import io.github.wtbyt298.accountbook.application.usecase.subaccounttitle.AppendSubAccountTitleUseCase;
import io.github.wtbyt298.accountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 補助科目追加処理のコントローラクラス
 */
@Controller
public class AppendSubAccountTitleController {

	@Autowired
	private AppendSubAccountTitleUseCase appendSubAccountTitleUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 補助科目を新規追加する
	 */
	@PostMapping("/accounttitle/append")
	public String append(@RequestParam String parentId, @RequestParam String subAccountTitleName) {
		AppendSubAccountTitleCommand command = new AppendSubAccountTitleCommand(parentId, subAccountTitleName);
		UserSession userSession = userSessionProvider.getUserSession();
		appendSubAccountTitleUseCase.execute(command, userSession);
		return "redirect:/accounttitle/detail/" + parentId;
	}
	
}

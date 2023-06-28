package wtbyt298.myaccountbook.presentation.controller.subaccounttitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.application.usecase.subaccounttitle.RenameSubAccountTitleCommand;
import wtbyt298.myaccountbook.application.usecase.subaccounttitle.RenameSubAccountTitleUseCase;
import wtbyt298.myaccountbook.presentation.shared.usersession.UserSessionProvider;

/**
 * 補助科目名変更処理のコントローラクラス
 */
@Controller
public class RenameSubAccountTitleController {

	@Autowired
	private RenameSubAccountTitleUseCase renameSubAccountTitleUseCase;
	
	@Autowired
	private UserSessionProvider userSessionProvider;
	
	/**
	 * 補助科目名を変更する
	 */
	@PostMapping("/accounttitle/edit")
	public String rename(@RequestParam String parentId, @RequestParam String subId, @RequestParam String newName) {
		RenameSubAccountTitleCommand command = new RenameSubAccountTitleCommand(parentId, subId, newName);
		UserSession userSession = userSessionProvider.getUserSession();
		
		renameSubAccountTitleUseCase.execute(command, userSession);
		
		return "redirect:/accounttitle/detail/" + parentId;
	}
	
}

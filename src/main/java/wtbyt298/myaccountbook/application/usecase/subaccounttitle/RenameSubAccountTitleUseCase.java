package wtbyt298.myaccountbook.application.usecase.subaccounttitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;

/**
 * 補助科目名の変更処理クラス
 */
@Service
public class RenameSubAccountTitleUseCase {

	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	/**
	 * 補助科目名を変更する
	 * @param 補助科目名変更用のDTO
	 */
	@Transactional
	public void execute(RenameSubAccountTitleCommand command, UserSession userSession) {
		//ドメインオブジェクトを生成
		AccountTitleId parentId = AccountTitleId.valueOf(command.getParentId());
		SubAccountTitles store = subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId());
		SubAccountTitleId subId = SubAccountTitleId.valueOf(command.getSubId());
		
		//補助科目名を変更する
		SubAccountTitleName newName = SubAccountTitleName.valueOf(command.getNewName());
		store.changeSubAccountTitleName(subId, newName);
		
		//リポジトリに保存する
		subAccountTitleRepository.save(store, userSession.userId());
	}
	
}

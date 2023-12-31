package wtbyt298.myaccountbook.application.usecase.subaccounttitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.application.shared.usersession.UserSession;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;

/**
 * 補助科目の追加処理クラス
 */
@Service
public class AppendSubAccountTitleUseCase {

	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	/**
	 * 補助科目を追加する
	 * @param command 補助科目追加用のDTO
	 */
	@Transactional
	public void execute(AppendSubAccountTitleCommand command, UserSession userSession) {
		//ドメインオブジェクトを生成
		AccountTitleId parentId = AccountTitleId.valueOf(command.getParentId());
		SubAccountTitles store = subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId());
		
		//補助科目を追加する
		SubAccountTitleName newName = SubAccountTitleName.valueOf(command.getNewName());
		store.add(newName);
		
		//リポジトリに保存する
		subAccountTitleRepository.save(store, userSession.userId());
	}
	
}

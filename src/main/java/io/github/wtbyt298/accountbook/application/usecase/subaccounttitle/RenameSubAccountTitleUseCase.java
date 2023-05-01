package io.github.wtbyt298.accountbook.application.usecase.subaccounttitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.accountbook.application.shared.usersession.UserSession;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;

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
		AccountTitleId parentId = AccountTitleId.valueOf(command.getParentId());
		SubAccountTitles store = subAccountTitleRepository.findCollectionByParentId(parentId, userSession.userId());
		SubAccountTitleId childId = SubAccountTitleId.valueOf(command.getChildId());
		SubAccountTitleName newName = SubAccountTitleName.valueOf(command.getNewName());
		store.changeSubAccountTitleName(childId, newName);
		subAccountTitleRepository.save(store, userSession.userId());
	}
	
}

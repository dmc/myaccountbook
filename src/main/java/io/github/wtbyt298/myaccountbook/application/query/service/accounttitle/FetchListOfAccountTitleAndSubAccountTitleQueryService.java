package io.github.wtbyt298.myaccountbook.application.query.service.accounttitle;

import java.util.*;

import io.github.wtbyt298.myaccountbook.application.query.model.accounttitle.AccountTitleAndSubAccountTitleDto;
import io.github.wtbyt298.myaccountbook.domain.model.user.UserId;

/**
 * 勘定科目IDと勘定科目名の一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface FetchListOfAccountTitleAndSubAccountTitleQueryService {

	List<AccountTitleAndSubAccountTitleDto> fetchAll(UserId userId);
	
}

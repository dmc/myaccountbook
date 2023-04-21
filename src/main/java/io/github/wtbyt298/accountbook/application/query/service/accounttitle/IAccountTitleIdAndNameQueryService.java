package io.github.wtbyt298.accountbook.application.query.service.accounttitle;

import java.util.*;

import io.github.wtbyt298.accountbook.application.query.model.accounttitle.AccountTitleIdAndNameDto;

/**
 * 勘定科目IDと勘定科目名の一覧取得用のインタフェース
 * 実装クラスはインフラ層に置く
 */
public interface IAccountTitleIdAndNameQueryService {

	List<AccountTitleIdAndNameDto> findAll();
	
}

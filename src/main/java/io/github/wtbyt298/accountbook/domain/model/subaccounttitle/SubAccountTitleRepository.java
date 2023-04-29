package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

/**
 * 補助科目集約のリポジトリ
 * 実装クラスはインフラ層に置く
 */
public interface SubAccountTitleRepository {

	void save();
	
	SubAccountTitles findByParentId();
	
}

package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.subaccounttitle;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.Tables.SUB_ACCOUNTTITLES;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.accountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.accountbook.domain.model.user.UserId;

/**
 * 補助科目のリポジトリクラス
 * 補助科目集約の永続化と再構築の詳細を記述する
 */
@Repository
class SubAccountTitleJooqRepository implements SubAccountTitleRepository {

	@Autowired
	private DSLContext jooq;
	
	/**
	 * 補助科目を保存する
	 */
	@Override
	public void save(SubAccountTitles subAccountTitles, UserId userId) {
		//補助科目IDは外部キー参照されていない
		//一旦DELETEして再度INSERTする実装とする
		jooq.deleteFrom(SUB_ACCOUNTTITLES)
			.where(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(subAccountTitles.parentId().value()))
			.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
			.execute();
		for (Entry<SubAccountTitleId, SubAccountTitle> each : subAccountTitles.elements().entrySet()) {
			insertRecord(each.getValue(), subAccountTitles.parentId(), userId);
		}
	}
	
	/**
	 * 補助科目テーブルにデータを挿入する
	 */
	private void insertRecord(SubAccountTitle subAccountTitle, AccountTitleId parentId, UserId userId) {
		jooq.insertInto(SUB_ACCOUNTTITLES)
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID, subAccountTitle.id().value())
			.set(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID, parentId.value())
			.set(SUB_ACCOUNTTITLES.USER_ID, userId.value())
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, subAccountTitle.name().value())
			.execute();
	}
	
	/**
	 * 勘定科目IDに一致する補助科目のコレクションを取得する
	 */
	@Override
	public SubAccountTitles findCollectionByParentId(AccountTitleId parentId, UserId userId) {
		Map<SubAccountTitleId, SubAccountTitle> subAccountTitles = new LinkedHashMap<>();
		Result<Record> result = jooq.select()
									.from(SUB_ACCOUNTTITLES)
									.where(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(parentId.value()))
									.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
									.fetch();
		if (result.isEmpty()) {
			return new SubAccountTitles(subAccountTitles, parentId); //補助科目が存在しない場合は、要素数0のコレクションを返す
		}
		for (Record each : result) {
			SubAccountTitleId id = SubAccountTitleId.valueOf(each.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID));
			SubAccountTitle subAccountTitle = mapRecordToEntity(each);
			subAccountTitles.put(id, subAccountTitle);
		}
		return new SubAccountTitles(subAccountTitles, parentId);
	}
	
	/**
	 * 補助科目のインスタンスを組み立てる
	 */
	private SubAccountTitle mapRecordToEntity(Record record) {
		return new SubAccountTitle(
			SubAccountTitleId.valueOf(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID)), 
			SubAccountTitleName.valueOf(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME))
		);
	}
	
	/**
	 * 勘定科目ID、補助科目ID、ユーザIDに一致する補助科目が存在するかどうかを判断する
	 */
	@Override
	public boolean exists(AccountTitleId parentId, SubAccountTitleId id, UserId userId) {
		Record result = jooq.select()
							.from(SUB_ACCOUNTTITLES)
							.where(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(parentId.value()))
								.and(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID.eq(id.value()))
								.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.value()))
							.fetchOne();
		if (id.value().equals("0") && result == null) return true; //補助科目を持たない場合、「0：補助科目なし」を持っていると考える
		if (result == null) return false; //補助科目を持っているが、該当のIDがテーブルに存在しない場合は単純にfalseを返す
		return true;
	}
	
}

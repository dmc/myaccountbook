package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.subaccounttitle;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import static generated.Tables.SUB_ACCOUNTTITLES;
import java.util.HashMap;
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
public class SubAccountTitleJooqRepository implements SubAccountTitleRepository {

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
			.where(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(subAccountTitles.parentId().toString()))
			.and(SUB_ACCOUNTTITLES.USER_ID.eq(""))
			.execute();
		for (Entry<SubAccountTitleId, SubAccountTitle> each : subAccountTitles.elements().entrySet()) {
			insertIntoTable(each.getValue(), subAccountTitles.parentId(), userId);
		}
	}
	
	/**
	 * 補助科目テーブルにデータを挿入する
	 */
	private void insertIntoTable(SubAccountTitle subAccountTitle, AccountTitleId parentId, UserId userId) {
		jooq.insertInto(SUB_ACCOUNTTITLES)
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID, subAccountTitle.id().toString())
			.set(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID, parentId.toString())
			.set(SUB_ACCOUNTTITLES.USER_ID, userId.toString())
			.set(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_NAME, subAccountTitle.name().toString())
			.execute();
	}

	/**
	 * 勘定科目IDに一致する補助科目のコレクションを取得する
	 */
	@Override
	public SubAccountTitles findCollectionByParentId(AccountTitleId parentId, UserId userId) {
		Map<SubAccountTitleId, SubAccountTitle> subAccountTitles = new HashMap<>();
		Result<Record> result = jooq.select()
									.from(SUB_ACCOUNTTITLES)
									.where(SUB_ACCOUNTTITLES.ACCOUNTTITLE_ID.eq(parentId.toString()))
									.and(SUB_ACCOUNTTITLES.USER_ID.eq(userId.toString()))
									.fetch();
		if (result.isEmpty()) {
			return new SubAccountTitles(subAccountTitles, parentId); //補助科目が存在しない場合は、要素数0のコレクションを返す
		}
		for (Record record : result) {
			SubAccountTitleId id = SubAccountTitleId.valueOf(record.get(SUB_ACCOUNTTITLES.SUB_ACCOUNTTITLE_ID));
			SubAccountTitle subAccountTitle = mapRecordToEntity(record);
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
	
}

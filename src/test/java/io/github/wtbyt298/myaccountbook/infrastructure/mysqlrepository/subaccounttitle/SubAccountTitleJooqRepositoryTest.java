package io.github.wtbyt298.myaccountbook.infrastructure.mysqlrepository.subaccounttitle;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitle;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleId;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleName;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitleRepository;
import io.github.wtbyt298.myaccountbook.domain.model.subaccounttitle.SubAccountTitles;
import io.github.wtbyt298.myaccountbook.domain.model.user.User;
import io.github.wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;
import io.github.wtbyt298.myaccountbook.helper.testdatacreator.UserTestDataCreator;
import io.github.wtbyt298.myaccountbook.helper.testfactory.SubAccountTitleTestFactory;

@SpringBootTest
@Transactional
class SubAccountTitleJooqRepositoryTest {

	@Autowired
	private SubAccountTitleRepository subAccountTitleRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Autowired
	private UserTestDataCreator userTestDataCreator;
	
	@Test
	void 補助科目のコレクションオブジェクトを保存した後に勘定科目を指定して取得できる() {
		//given:勘定科目とユーザが既に作成されている
		AccountTitle parent = accountTitleTestDataCreator.create("000", "テスト用の勘定科目",AccountingType.ASSETS);
		User user = userTestDataCreator.create();
		
		//保存するコレクションオブジェクトの設定（補助科目を2つ保持している）
		Map<SubAccountTitleId, SubAccountTitle> map = new HashMap<>();		
		map.put(SubAccountTitleId.valueOf("0"), SubAccountTitleTestFactory.create("0", "その他"));
		map.put(SubAccountTitleId.valueOf("1"), SubAccountTitleTestFactory.create("1", "テスト用の補助科目"));
		SubAccountTitles store = new SubAccountTitles(map, parent.id());
		assertEquals(2, store.elements().size());
		
		//when:コレクションオブジェクトを保存し、その後勘定科目IDで検索して取得する
		subAccountTitleRepository.save(store, user.id());
		SubAccountTitles found = subAccountTitleRepository.findCollectionByParentId(parent.id(), user.id());
		
		//then:保存した補助科目のコレクションオブジェクトをリポジトリ経由で取得できる
		assertAll(
			() -> assertEquals(2, found.elements().size()),
			() -> assertEquals(parent.id(), found.parentId()),
			() -> assertEquals(store.find(SubAccountTitleId.valueOf("0")).toString(), found.find(SubAccountTitleId.valueOf("0")).toString()),
			() -> assertEquals(store.find(SubAccountTitleId.valueOf("1")).toString(), found.find(SubAccountTitleId.valueOf("1")).toString())
		);
	}
	
	@Test
	void 補助科目が存在しない場合は空のコレクションオブジェクトを返す() {
		//given:勘定科目とユーザが既に作成されている
		AccountTitle parent = accountTitleTestDataCreator.create("000", "テスト用の勘定科目",AccountingType.ASSETS);
		User user = userTestDataCreator.create();
		//補助科目は追加しない
		SubAccountTitles store = new SubAccountTitles(new HashMap<>(), parent.id());
				
		//when:コレクションオブジェクトを保存し、その後勘定科目IDで検索して取得する
		subAccountTitleRepository.save(store, user.id());
		SubAccountTitles found = subAccountTitleRepository.findCollectionByParentId(parent.id(), user.id());
		
		//then:空のコレクションオブジェクトをリポジトリ経由で取得できる
		assertEquals(0, found.elements().size());
		assertEquals("補助科目ID：0 補助科目名：補助科目なし", found.find(SubAccountTitleId.valueOf("0")).toString());
	}
	
	@Test
	void 補助科目を持たない場合の存在チェックメソッドのテスト() {
		//given:勘定科目とユーザが既に作成されている
		AccountTitle parent = accountTitleTestDataCreator.create("000", "テスト用の勘定科目",AccountingType.ASSETS);
		User user = userTestDataCreator.create();
		
		//when:検索対象の補助科目を用意する
		SubAccountTitle empty = SubAccountTitleTestFactory.create("0", "補助科目なし");
		SubAccountTitle notExist = SubAccountTitleTestFactory.create("1", "テスト用の補助科目");
		
		//then:補助科目ID「0」の場合はtrueを返す
		//それ以外はfalseを返す
		assertTrue(subAccountTitleRepository.exists(parent.id(), empty.id(), user.id()));
		assertFalse(subAccountTitleRepository.exists(parent.id(), notExist.id(), user.id()));
	}
	
	@Test
	void 補助科目を持つ場合の存在チェックメソッドのテスト() {
		//given:勘定科目とユーザが既に作成されている
		AccountTitle parent = accountTitleTestDataCreator.create("000", "テスト用の勘定科目",AccountingType.ASSETS);
		User user = userTestDataCreator.create();
		
		//補助科目が追加されている
		SubAccountTitles store = subAccountTitleRepository.findCollectionByParentId(parent.id(), user.id());
		store.add(SubAccountTitleName.valueOf("テスト用の補助科目"));
		subAccountTitleRepository.save(store, user.id());
		
		//when:検索対象の補助科目を用意する
		SubAccountTitle element1 = store.find(SubAccountTitleId.valueOf("0")); //0：その他
		SubAccountTitle element2 = store.find(SubAccountTitleId.valueOf("1")); //1：テスト用の補助科目
		
		//then:補助科目ID「0」「1」の場合はtrueを返す
		//存在しない補助科目IDを指定した場合はfalseを返す
		assertTrue(subAccountTitleRepository.exists(parent.id(), element1.id(), user.id()));
		assertTrue(subAccountTitleRepository.exists(parent.id(), element2.id(), user.id()));
		assertFalse(subAccountTitleRepository.exists(parent.id(), SubAccountTitleId.valueOf("2"), user.id()));
	}
	
}

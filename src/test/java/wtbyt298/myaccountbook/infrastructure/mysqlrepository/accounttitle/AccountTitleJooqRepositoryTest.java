package wtbyt298.myaccountbook.infrastructure.mysqlrepository.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wtbyt298.myaccountbook.domain.model.accountingelement.AccountingType;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitle;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleId;
import wtbyt298.myaccountbook.domain.model.accounttitle.AccountTitleRepository;
import wtbyt298.myaccountbook.helper.testdatacreator.AccountTitleTestDataCreator;

@SpringBootTest
@Transactional
class AccountTitleJooqRepositoryTest {

	@Autowired
	private AccountTitleRepository accountTitleRepository;
	
	@Autowired
	private AccountTitleTestDataCreator accountTitleTestDataCreator;
	
	@Test
	void IDを指定して勘定科目を取得できる() {
		//given:既に勘定科目が作成されている
		AccountTitle createdAccountTitle = accountTitleTestDataCreator.create("000", "テスト用勘定科目", AccountingType.ASSETS);
		AccountTitleId id = createdAccountTitle.id();
		assertTrue(accountTitleRepository.exists(id));
		
		//when:IDで勘定科目を検索して取得する
		AccountTitle foundAccountTitle = accountTitleRepository.findById(id);
		
		//then:保存した勘定科目をリポジトリ経由で取得できる
		assertAll(
			() -> assertEquals(createdAccountTitle.id(), foundAccountTitle.id()),
			() -> assertEquals(createdAccountTitle.name(), foundAccountTitle.name()),
			() -> assertEquals(createdAccountTitle.accountingType(), foundAccountTitle.accountingType())
		);
	}

	@Test
	void 存在しない勘定科目を取得しようとすると例外発生() {
		//given:勘定科目ID"999"に該当するデータは存在しない
		AccountTitleId id = AccountTitleId.valueOf("999");
		assertFalse(accountTitleRepository.exists(id));
		
		//when:IDで勘定科目を検索して取得する
		Exception exception = assertThrows(RuntimeException.class, () -> accountTitleRepository.findById(id));
		
		//then:想定した例外が発生している
		assertEquals("指定した勘定科目は存在しません。", exception.getMessage());
	}
	
	@Test
	void findAllメソッドで全ての勘定科目を取得できる() {
		//given:勘定科目が複数作成されている
		AccountTitle created1 = accountTitleTestDataCreator.create("001", "科目1", AccountingType.ASSETS);
		AccountTitle created2 = accountTitleTestDataCreator.create("002", "科目2", AccountingType.LIABILITIES);
		AccountTitle created3 = accountTitleTestDataCreator.create("003", "科目3", AccountingType.EQUITY);
		AccountTitle created4 = accountTitleTestDataCreator.create("004", "科目4", AccountingType.EXPENSES);
		AccountTitle created5 = accountTitleTestDataCreator.create("005", "科目5", AccountingType.REVENUE);
		List<AccountTitle> created = Arrays.asList(created1, created2, created3, created4, created5);
		
		//when:findAllメソッドを実行して勘定科目のリストを取得する
		List<AccountTitle> found = accountTitleRepository.findAll();
		
		//then:作成した勘定科目と取得した勘定科目の各属性が一致する
		for (int i = 0; i < created.size(); i++) {
			assertEquals(found.get(i).id(), created.get(i).id());
			assertEquals(found.get(i).name(), created.get(i).name());
			assertEquals(found.get(i).accountingType(), found.get(i).accountingType());
		}
	}
	
}

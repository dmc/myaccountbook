package io.github.wtbyt298.accountbook.infrastructure.mysqlrepository.accounttitle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitle;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleRepository;
import io.github.wtbyt298.accountbook.helper.testdatacreator.AccountTitleTestDataCreator;

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
		
		//when:IDで勘定科目を検索して取得する
		AccountTitle foundAccountTitle = accountTitleRepository.findById(id);
		
		//then:保存した勘定科目をリポジトリ経由で取得できる
		assertEquals(createdAccountTitle.id(), foundAccountTitle.id());
		assertEquals(createdAccountTitle.name(), foundAccountTitle.name());
		assertEquals(createdAccountTitle.accountingType(), foundAccountTitle.accountingType());
	}

	@Test
	void IDに一致する勘定科目が存在しない場合は例外発生() {
		//given:勘定科目ID"999"に該当するデータは存在しない
		AccountTitleId id = AccountTitleId.valueOf("999");
		
		//when:IDで勘定科目を検索して取得する
		Exception exception = assertThrows(RuntimeException.class, () -> accountTitleRepository.findById(id));
		
		//then:想定した例外が発生している
		assertEquals("指定した勘定科目は存在しません。", exception.getMessage());
	}
	
}

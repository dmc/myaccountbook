package io.github.wtbyt298.accountbook.application.query.model.summary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.wtbyt298.accountbook.domain.model.accountingelement.AccountingType;
import io.github.wtbyt298.accountbook.domain.model.accounttitle.AccountTitleId;

class FinancialStatementTest {

	@Test
	void 会計区分ごとの合計金額を計算できる() {
		//given:会計区分は複数存在している
		AccountTitleId id = AccountTitleId.valueOf("101");
		List<MonthlyBalanceDto> elements = new ArrayList<>();
		//資産の合計は3000
		elements.add(createTestMonthlyBalanceDto(id, "", AccountingType.ASSETS, 1000));
		elements.add(createTestMonthlyBalanceDto(id, "", AccountingType.ASSETS, 2000));
		//負債の合計は1500
		elements.add(createTestMonthlyBalanceDto(id, "", AccountingType.LIABILITIES, 1000));
		elements.add(createTestMonthlyBalanceDto(id, "", AccountingType.LIABILITIES, 500));
		FinancialStatement fs = new FinancialStatement(elements);
		
		//when:会計区分ごとに合計金額を計算する
		final int totalOfAssets = fs.calculateTotalAmount(AccountingType.ASSETS);
		final int totalOfLiabilities = fs.calculateTotalAmount(AccountingType.LIABILITIES);
		
		//then:合計金額が正しく計算されている
		assertEquals(3000, totalOfAssets);
		assertEquals(1500, totalOfLiabilities);
	}
	
	@Test
	void 該当する会計区分がない場合は0を返す() {
		//given:内訳のDTOが空である
		FinancialStatement fs = new FinancialStatement(new ArrayList<>());
		
		//when:会計区分を指定して合計金額を計算する
		final int totalOfAssets = fs.calculateTotalAmount(AccountingType.ASSETS);
		
		//then:合計金額は0である
		assertEquals(0, totalOfAssets);
	}
	
	@Test
	void 保持しているリストを勘定科目ごとに絞り込んで返す() {
		//given:勘定科目IDは複数存在する
		AccountTitleId id1 = AccountTitleId.valueOf("101");
		AccountTitleId id2 = AccountTitleId.valueOf("102");
		List<MonthlyBalanceDto> elements = new ArrayList<>();
		elements.add(new MonthlyBalanceDto(id1, "", AccountingType.ASSETS, 1000));
		elements.add(new MonthlyBalanceDto(id1, "", AccountingType.ASSETS, 1000));
		elements.add(new MonthlyBalanceDto(id2, "", AccountingType.ASSETS, 1000));
		elements.add(new MonthlyBalanceDto(id2, "", AccountingType.ASSETS, 1000));
		FinancialStatement fs = new FinancialStatement(elements);
		
		//when:勘定科目ごとに絞り込んだリストを取得する
		List<MonthlyBalanceDto> data1 = fs.filteredByAccountTitle(id1);
		List<MonthlyBalanceDto> data2 = fs.filteredByAccountTitle(id2);
		
		//then:DTOの勘定科目IDはそれぞれ指定したIDと一致する
		//この場合それぞれの要素数は2である
		data1.stream().forEach(each -> assertEquals(id1, each.getAccountTitleId())) ;
		data2.stream().forEach(each -> assertEquals(id2, each.getAccountTitleId()));
		assertEquals(2, data1.size());
		assertEquals(2, data2.size());
	}
	
	@Test
	void 該当する勘定科目がなければ空のリストを返す() {
		//given:内訳のDTOが空である
		FinancialStatement fs = new FinancialStatement(new ArrayList<>());
		
		//when:勘定科目IDを指定してDTOのリストを取り出す
		List<MonthlyBalanceDto> data = fs.filteredByAccountTitle(AccountTitleId.valueOf("101"));
		
		//then:リストの要素数は0である
		assertEquals(0, data.size());
	}
	
	/**
	 * テスト用のDTOのインスタンスを生成する
	 */
	private MonthlyBalanceDto createTestMonthlyBalanceDto(AccountTitleId id, String subAccountTitleName, AccountingType accountingType, int balance) {
		return new MonthlyBalanceDto(id, subAccountTitleName, accountingType, balance);
	}

}

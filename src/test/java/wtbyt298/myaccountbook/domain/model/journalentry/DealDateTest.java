package wtbyt298.myaccountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.YearMonth;
import org.junit.jupiter.api.Test;

import wtbyt298.myaccountbook.domain.model.journalentry.DealDate;

class DealDateTest {

	@Test
	void LocalDate型で初期化できる() {
		//when:
		DealDate date = DealDate.valueOf(LocalDate.of(2023, 4, 1));
		
		//then:
		assertEquals(LocalDate.of(2023, 4, 1), date.value);
	}
	
	@Test
	void 日付型から年月型に変換できる() {
		//when:
		DealDate date = DealDate.valueOf(LocalDate.of(2023, 4, 1));
		
		//then:
		assertEquals(YearMonth.of(2023, 4), date.toYearMonth());
	}
	
	@Test
	void 保持している日付が同一なら等価判定されハッシュ値も等しくなる() {
		//when:
		DealDate a = DealDate.valueOf(LocalDate.now());
		DealDate b = DealDate.valueOf(LocalDate.now());
		DealDate c = DealDate.valueOf(LocalDate.now().plusDays(1));
		
		//then:
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertNotEquals(b, c);
		assertNotEquals(b.hashCode(), c.hashCode());
	}
	
}

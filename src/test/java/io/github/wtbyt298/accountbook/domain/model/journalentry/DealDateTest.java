package io.github.wtbyt298.accountbook.domain.model.journalentry;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DealDateTest {

	@Test
	void LocalDate型で初期化できる() {
		DealDate date = DealDate.valueOf(LocalDate.of(2023, 4, 1));
		assertEquals("2023-04-01", date.toString());
	}
	
	@Test
	void 年月をyyyyMM形式の文字列として出力する() {
		DealDate date = DealDate.valueOf(LocalDate.of(2023, 4, 1));
		assertEquals("202304", date.yearMonth());
	}

}

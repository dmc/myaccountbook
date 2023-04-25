package io.github.wtbyt298.accountbook.presentation.controller.journalentry;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.github.wtbyt298.accountbook.application.query.model.journalentry.JournalEntryDto;
import io.github.wtbyt298.accountbook.application.query.service.journalentry.FetchJournalEntryListQueryService;

/**
 * 仕訳一覧取得処理のコントローラ
 */
@Controller
public class FetchJournalEntryListController {

	@Autowired
	private FetchJournalEntryListQueryService fetchQueryService;
	
	/**
	 * 年月を指定して仕訳の一覧を取得する
	 */
	@GetMapping("/entries/{selectedYearMonth}")
	public String entries(@PathVariable String selectedYearMonth, Model model) {
		YearMonth yearMonth = YearMonth.parse(selectedYearMonth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		List<JournalEntryDto> journalEntryDtoList = fetchQueryService.findAll(yearMonth);
		//Viewモデルへの詰め替え処理
		//modelの属性にViewを追加する
		return "entries";
	}
	
}

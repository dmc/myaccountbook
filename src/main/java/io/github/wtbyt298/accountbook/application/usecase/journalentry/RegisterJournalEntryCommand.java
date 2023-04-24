package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

/**
 * 仕訳登録用のDTOクラス
 */
@Getter
public class RegisterJournalEntryCommand {
	
	private final LocalDate dealDate; //取引日
	private final String description; //摘要
	private final List<RegisterEntryDetailCommand> detailCommands; //仕訳明細のリスト
	
	public RegisterJournalEntryCommand(LocalDate dealDate, String description, List<RegisterEntryDetailCommand> detailCommands) {
		this.dealDate = dealDate;
		this.description = description;
		this.detailCommands = detailCommands;
	}
	
}

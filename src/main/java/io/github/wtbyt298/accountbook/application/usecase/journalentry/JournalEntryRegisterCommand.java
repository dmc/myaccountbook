package io.github.wtbyt298.accountbook.application.usecase.journalentry;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

/**
 * 仕訳登録用のDTOクラス
 */
@Getter
public class JournalEntryRegisterCommand {
	
	private final String entryId;
	private final LocalDate dealDate;
	private final String description;
	private final List<EntryDetailRegisterCommand> detailCommands;
	
	public JournalEntryRegisterCommand(String entryId, LocalDate dealDate, String description, List<EntryDetailRegisterCommand> detailCommands) {
		this.entryId = entryId;
		this.dealDate = dealDate;
		this.description = description;
		this.detailCommands = detailCommands;
	}
	
}

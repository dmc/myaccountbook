package io.github.wtbyt298.accountbook.presentation.httprequest.journalentry;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 仕訳登録用のDTO
 * JournalEntryRegisterParamの部品
 */
@Data
public class JournalEntryDetailParam {

	@NotBlank
	private String detailLoanType;
	
	@NotBlank(message = "勘定科目を選択してください。")
	private String accountTitleId;
	
	@NotNull(message = "金額を入力してください。")
	@Min(value = 1, message = "金額は1以上で入力してください。")
	private Integer amount;
	
}
 
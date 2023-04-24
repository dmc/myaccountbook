package io.github.wtbyt298.accountbook.presentation.request.journalentry;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

/**
 * 仕訳登録画面のフォームクラス
 */
@Data
public class RegisterJournalEntryParam {

	@NotNull(message = "取引日を入力してください。")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dealDate;
	
	@NotBlank(message = "摘要を入力してください。")
	@Size(max = 32, message = "摘要は32文字以内で入力してください。")
	private String description;

	@Valid
	private List<RegisterEntryDetailParam> detailParams;
	
}

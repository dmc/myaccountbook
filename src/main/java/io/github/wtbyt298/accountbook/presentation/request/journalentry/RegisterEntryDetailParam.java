package io.github.wtbyt298.accountbook.presentation.request.journalentry;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 仕訳登録画面のフォームクラス
 * RegisterjournalEntryParamの子要素
 */
@Data
public class RegisterEntryDetailParam {

	@NotBlank
	private String detailLoanType;
	
	@NotBlank(message = "勘定科目を選択してください。")
	private String mergedId; //例"101-0"　※"勘定科目ID-補助科目ID"の形式
	
	@NotNull(message = "金額を入力してください。")
	@Min(value = 1, message = "金額は1以上で入力してください。")
	private Integer amount;
	
	/**
	 * "101-0"から"101"を取り出す
	 * @return 勘定科目ID
	 */
	public String getAccountTitleId() {
		return mergedId.substring(0, mergedId.indexOf("-"));
	}
	
	/**
	 * "101-0"から"0"を取り出す
	 * @return 補助科目ID
	 */
	public String getSubAccountTitleId() {
		return mergedId.substring(mergedId.indexOf("-"), mergedId.length());
	}
	
}
 
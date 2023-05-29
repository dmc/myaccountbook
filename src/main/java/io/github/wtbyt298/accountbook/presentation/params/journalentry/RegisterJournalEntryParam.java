package io.github.wtbyt298.accountbook.presentation.params.journalentry;

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
	
	private static int MIN_LIST_SIZE = 1;
	private static int MAX_LIST_SIZE = 10;

	@NotNull(message = "取引日を入力してください。")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dealDate;
	
	@NotBlank(message = "摘要を入力してください。")
	@Size(max = 32, message = "摘要は32文字以内で入力してください。")
	private String description;

	@Valid
	private List<RegisterEntryDetailParam> debitParams;
	
	@Valid
	private List<RegisterEntryDetailParam> creditParams;
	
	public RegisterJournalEntryParam(LocalDate dealDate, String description, List<RegisterEntryDetailParam> debitParams, List<RegisterEntryDetailParam> creditParams) {
		this.dealDate = dealDate;
		this.description = description;
		this.debitParams = debitParams;
		this.creditParams = creditParams;
	}
	
	public List<RegisterEntryDetailParam> getEntryDetailParams() {
		List<RegisterEntryDetailParam> list = new ArrayList<>(debitParams);
		list.addAll(creditParams);
		return list;
	}
	
	/**
	 * 仕訳明細パラメータを初期化する
	 */
	public void initList() {
		debitParams = new ArrayList<>();
		creditParams = new ArrayList<>();
		debitParams.add(new RegisterEntryDetailParam());
		creditParams.add(new RegisterEntryDetailParam());
	}
	
	/**
	 * 仕訳明細パラメータを追加する
	 */
	public void addList(String type) {
		if (type.equals("DEBIT")) {
			if (debitParams.size() >= MAX_LIST_SIZE) return;
			debitParams.add(new RegisterEntryDetailParam());
		}
		if (type.equals("CREDIT")) {
			if (creditParams.size() >= MAX_LIST_SIZE) return;
			creditParams.add(new RegisterEntryDetailParam());
		}
	}
	
	/**
	 * 仕訳明細パラメータを削除する
	 */
	public void removeList(String type, int index) {
		if (type.equals("DEBIT")) {
			if (debitParams.size() <= MIN_LIST_SIZE) return;
			debitParams.remove(index);
		}
		if (type.equals("CREDIT")) {
			if (creditParams.size() <= MIN_LIST_SIZE) return;
			creditParams.remove(index);
		}
	}
	
	/**
	 * リストが最大数を超えて追加されているかを判断する
	 */
	public boolean isFull(String type) {
		Objects.requireNonNull(type);
		if (type.equals("DEBIT")) return debitParams.size() >= MAX_LIST_SIZE;
		if (type.equals("CREDIT")) return creditParams.size() >= MAX_LIST_SIZE;
		return false;
	}
	
}

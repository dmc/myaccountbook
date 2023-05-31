package io.github.wtbyt298.accountbook.presentation.forms.journalentry;

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
public class RegisterJournalEntryForm {
	
	private static int MIN_LIST_SIZE = 1;
	private static int MAX_LIST_SIZE = 10;

	@NotNull(message = "取引日を入力してください。")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dealDate;
	
	@NotBlank(message = "摘要を入力してください。")
	@Size(max = 32, message = "摘要は32文字以内で入力してください。")
	private String description;

	@Valid
	private List<RegisterEntryDetailForm> debitForms;
	
	@Valid
	private List<RegisterEntryDetailForm> creditForms;
	
	public RegisterJournalEntryForm(LocalDate dealDate, String description, List<RegisterEntryDetailForm> debitForms, List<RegisterEntryDetailForm> creditForms) {
		this.dealDate = dealDate;
		this.description = description;
		this.debitForms = debitForms;
		this.creditForms = creditForms;
	}
	
	public List<RegisterEntryDetailForm> getEntryDetailParams() {
		List<RegisterEntryDetailForm> list = new ArrayList<>(debitForms);
		list.addAll(creditForms);
		return list;
	}
	
	/**
	 * 仕訳明細パラメータを初期化する
	 */
	public void initList() {
		debitForms = new ArrayList<>();
		creditForms = new ArrayList<>();
		debitForms.add(new RegisterEntryDetailForm());
		creditForms.add(new RegisterEntryDetailForm());
	}
	
	/**
	 * 仕訳明細パラメータを追加する
	 */
	public void addList(String type) {
		if (type.equals("DEBIT")) {
			if (debitForms.size() >= MAX_LIST_SIZE) return;
			debitForms.add(new RegisterEntryDetailForm());
		}
		if (type.equals("CREDIT")) {
			if (creditForms.size() >= MAX_LIST_SIZE) return;
			creditForms.add(new RegisterEntryDetailForm());
		}
	}
	
	/**
	 * 仕訳明細パラメータを削除する
	 */
	public void removeList(String type, int index) {
		if (type.equals("DEBIT")) {
			if (debitForms.size() <= MIN_LIST_SIZE) return;
			debitForms.remove(index);
		}
		if (type.equals("CREDIT")) {
			if (creditForms.size() <= MIN_LIST_SIZE) return;
			creditForms.remove(index);
		}
	}
	
	/**
	 * リストが最大数を超えて追加されているかを判断する
	 */
	public boolean isFull(String type) {
		Objects.requireNonNull(type);
		if (type.equals("DEBIT")) return debitForms.size() >= MAX_LIST_SIZE;
		if (type.equals("CREDIT")) return creditForms.size() >= MAX_LIST_SIZE;
		return false;
	}
	
}

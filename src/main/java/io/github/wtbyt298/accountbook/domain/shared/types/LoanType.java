package io.github.wtbyt298.accountbook.domain.shared.types;

/**
 * 貸借区分
 */
public enum LoanType {
	
	DEBIT,   //借方
	CREDIT;  //貸方

	public String label() {
		if (this.equals(DEBIT)) return "借方";
		return "貸方";
	}
	
}

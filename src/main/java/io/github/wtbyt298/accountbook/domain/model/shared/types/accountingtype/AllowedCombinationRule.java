package io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype;

import static io.github.wtbyt298.accountbook.domain.model.shared.types.accountingtype.AccountingType.*;

import java.util.*;

/**
 * 会計区分の貸借の可能組み合わせのルールを表現するクラス
 */
public class AllowedCombinationRule {

	//key：借方会計区分
	//value：keyに対して組み合わせ可能な貸方会計区分
	private static Map<AccountingType, Set<AccountingType>> allowed; 
	
	static {
		allowed = new HashMap<>();
		allowed.put(ASSETS, EnumSet.of(ASSETS, LIABILITIES, NETASSETS, REVENUE));
		allowed.put(LIABILITIES, EnumSet.of(ASSETS, LIABILITIES, REVENUE));
		allowed.put(NETASSETS, EnumSet.of(ASSETS, NETASSETS));
		allowed.put(EXPENSES, EnumSet.of(ASSETS, LIABILITIES, NETASSETS, REVENUE));
		allowed.put(REVENUE, EnumSet.noneOf(AccountingType.class)); //収益は貸方にのみ指定可能
	}
	
	/**
	 * @param debit 借方会計区分
	 * @param credit 貸方会計区分
	 * @return 組み合わせ可能である場合true
	 */
	public static boolean ok(AccountingType debit, AccountingType credit) {
		Set<AccountingType> allowedTypes = allowed.get(debit);
		return allowedTypes.contains(credit);
	}
	
}

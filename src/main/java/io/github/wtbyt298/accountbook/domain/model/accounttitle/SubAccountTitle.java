package io.github.wtbyt298.accountbook.domain.model.accounttitle;

/**
 * 補助科目クラス
 */
public class SubAccountTitle {
	
	static final SubAccountTitle EMPTY = new SubAccountTitle(SubAccountTitleId.valueOf("0"), SubAccountTitleName.valueOf("補助科目なし"));
	final SubAccountTitleId id;
	final SubAccountTitleName name;
	
	SubAccountTitle(SubAccountTitleId id, SubAccountTitleName name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * @return 補助科目ID
	 */
	public String id() {
		return id.value;
	}
	
	/**
	 * @return 補助科目名
	 */
	public String name() {
		return name.value;
	}
	
	@Override
	public String toString() {
		return id.toString() + "：" + name.toString();
	}
	
}

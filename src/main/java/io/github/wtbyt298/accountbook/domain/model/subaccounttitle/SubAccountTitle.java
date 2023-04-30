package io.github.wtbyt298.accountbook.domain.model.subaccounttitle;

import java.util.Objects;

/**
 * 補助科目クラス
 */
public class SubAccountTitle {
	
	static final SubAccountTitle EMPTY = new SubAccountTitle(SubAccountTitleId.valueOf("0"), SubAccountTitleName.valueOf("補助科目なし"));
	private final SubAccountTitleId id;
	private SubAccountTitleName name;
	
	public SubAccountTitle(SubAccountTitleId id, SubAccountTitleName name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * @return 補助科目ID
	 */
	public SubAccountTitleId id() {
		return id;
	}
	
	/**
	 * 補助科目名を変更する
	 */
	void rename(SubAccountTitleName newName) {
		name = newName;
	}
	
	@Override
	public String toString() {
		return id.toString() + "：" + name.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (! (obj instanceof SubAccountTitle)) return false;
		SubAccountTitle other = (SubAccountTitle) obj;
		return this.id.equals(other.id) && this.name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name);
	}
	
}

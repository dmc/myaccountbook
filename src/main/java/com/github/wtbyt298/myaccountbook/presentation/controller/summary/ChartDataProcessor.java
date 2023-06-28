package com.github.wtbyt298.myaccountbook.presentation.controller.summary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;

/**
 * グラフ表示用のデータを加工するクラス
 */
public class ChartDataProcessor {

	private final List<Entry<String, BigDecimal>> data;
	
	public ChartDataProcessor(List<Entry<String, BigDecimal>> data) {
		//金額の降順で並べ替えておく
		data.sort((o1, o2) -> (-o1.getValue().compareTo(o2.getValue())));
		this.data = data;
	}
	
	/**
	 * @return 勘定科目名の配列
	 */
	public String[] labels() {
		//Keyのみを取り出す
		List<String> keys = data.stream()
			.map(each -> each.getKey())
			.toList();
		
		return keys.toArray(new String[keys.size()]);
	}
	
	/**
	 * @return 金額の配列
	 */
	public BigDecimal[] values() {
		//Valueのみを取り出す
		List<BigDecimal> values = data.stream()
			.map(each -> each.getValue())
			.toList();
		
		return values.toArray(new BigDecimal[values.size()]);
	}
	
	/**
	 * @return 合計金額
	 */
	public int total() {
		
		return data.stream()
			.mapToInt(each -> each.getValue().intValue())
			.sum();
	}
	
}

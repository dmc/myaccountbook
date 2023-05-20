/**
 * 伝票を並べ替える
 */
function sortEntries(key) {
	switch (key) {
		case 'date':
			location.href = '?orderKey=DEAL_DATE';
			break;
		case 'amount':
			location.href = '?orderKey=TOTAL_AMOUNT';
			break;
	}
	
}

/**
 * ラジオボタンにチェックを入れる
 */
window.addEventListener('load', function() {
	var url = location.search;
	if (url == '?orderKey=DEAL_DATE') {
		document.getElementById('btnradio1').checked = true;
		return;
	}
	if (url == '?orderKey=TOTAL_AMOUNT') {
		document.getElementById('btnradio2').checked = true;
		return;
	}
	document.getElementById('btnradio1').checked = true;
});
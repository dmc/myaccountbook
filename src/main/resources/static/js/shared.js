/**
 * 月別仕訳一覧画面へのリンクを作成する
 */
window.addEventListener('load', function() {
	var link = document.getElementById('entries');
	var url = '/entry/entries/' + currentYearMonth();
	link.setAttribute('href', url);
});

/**
 * 月別収支一覧画面へのリンクを作成する
 */
window.addEventListener('load', function() {
	var link = document.getElementById('pl');
	var url = '/summary/pl/' + currentYearMonth();
	link.setAttribute('href', url);
});

/**
 * 月別資産状況一覧画面へのリンクを作成する
 */
window.addEventListener('load', function() {
	var link = document.getElementById('bs');
	var url = '/summary/bs/' + currentYearMonth();
	link.setAttribute('href', url);
});

/**
 * 現在の年月を表す文字列を作成する
 */
function currentYearMonth() {
	var now = new Date();
    var yyyy = now.getFullYear();
    var mm = ('0' + (now.getMonth() + 1)).slice(-2);
    return yyyy + '-' + mm;	
}

/**
 * 選択した年月のページに切り替える
 */
function selectMonth() {
	var selected = document.getElementById('input-month');
	location.href = selected.value;
}	
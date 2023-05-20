/**
 * 現在の年月日を表す文字列を作成する
 */
window.addEventListener('load', function() {
	var now = new Date();
    var yyyy = now.getFullYear();
    var mm = ('0' + (now.getMonth() + 1)).slice(-2);
    var dd = ('0' + now.getDate()).slice(-2);
    
    var form_date = document.getElementById('form-date');
	form_date.value = yyyy + '-' + mm + '-' + dd;
});
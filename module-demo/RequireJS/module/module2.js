define(function(){
	var printModule2FileName = function(){
		document.write('This is module2<br/>');
	}
	return {
		filename:"module2",
		printModule2FileName:printModule2FileName
	};
});
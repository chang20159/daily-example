define(function(){
	function printModule1FileName(){
		document.write('This is module1<br/>');
	}
	return {
		filename:"module1",
		printModule1FileName:printModule1FileName
	};
});

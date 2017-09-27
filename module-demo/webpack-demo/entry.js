require("!style!css!./style/style.css")
var printModule1 = require('./module/module1.js')
var printModule2 = require('./module/module2.js')

function printEntryFileName(){	
	printModule1();
	printModule2();
}

printEntryFileName();

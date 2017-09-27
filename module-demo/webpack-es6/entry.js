import "!style!css!./style/style.css"
import * as Module1 from './module/module1.js'
import * as Module2 from './module/module2.js'

import "!style!css!./style/style.css"
	import * as Module1 from './module/module1.js'
	import * as Module2 from './module/module2.js'
	
	class Entry{
		constructor(filename1,filename2){
			this.filename1 = filename1;
			this.filename2 = filename2;		
		}
	
		printModuleFileName(){
			Module1.printModule1FileName(this.filename1);
			Module2.printModule2FileName(this.filename2);
		}
	}
	
	var entry = new Entry("es6module1","es6module2");
	entry.printModuleFileName();

// class Entry{
// 	constructor(filename1,filename2){
// 		Module1.printModule1FileName(filename1);
// 		Module2.printModule2FileName(filename2);
// 	}
// }

// (() => new Entry("es6module1","es6module1"))();


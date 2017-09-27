require.config({
	path: {
		module: './module',
	}
});
require(['module/module1.js','module/module2.js'],function(module1,module2){
	document.write(`${module1.filename} and ${module2.filename}<br/>`);
    module1.printModule1FileName();
    module2.printModule2FileName();
});

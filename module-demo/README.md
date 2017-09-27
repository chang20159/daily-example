关于javascript模块化编程的介绍可以参考[阮一峰的Javascript模块化编程系列](http://www.ruanyifeng.com/blog/2012/10/asynchronous_module_definition.html)和[webpack中文指南](http://zhaoda.net/webpack-handbook/index.html)，详细讲述了模块系统的演进以及各种方式的使用方法。看完之后特来实践一下。
<!-- more -->
## 原始的JS文件加载
来看下在html中用script标签引入js文件的情况      
  
新建一个文件目录   `mkdir ScriptTag`    
   
进入此目录   `cd ScriptTag`
 
在此文件中新建文件    `touch index.html`       
  
内容如下：    

    <html>
	    <head>
			<title>webpack</title>
			<meta charset="utf-8">
	    </head>
	    <body>
	        <script src="module1.js"></script>
	        <script src="module2.js"></script>
	
		    <script type="text/javascript">
				printModule1FileName();
				printModule2FileName();
		    </script>
	    </body>
    </html>
   

新建module1.js  

```javascript       
var filename = "module1";
function printModule1FileName(){
	document.write(`This is ${filename}<br/>`);
}
```
新建module2.js      
    
```javascript       
var filename = "module2";
function printModule2FileName(){
	document.write(`This is ${filename}<br/>`);
}
```    
打开index.html文件，浏览器中显示:  
  
```
This is module2    
This is module2  
```   
将index.html中引入的js文件交换顺序   
 
```xml   
<script src="module2.js"></script>
<script src="module1.js"></script>    
```
此时浏览器中显示：  

```  
This is module1    
This is module1     
```
使用这种方式引入js文件，每个js文件中的变量和方法都暴露在全局作用域下，并按照js文件的引入顺序加载，所以当module1和module2中都有filename这个变量且最后引入module2时，加载完成后的filename最终指向的是module2.js中定义的字符串“module2”。当引入的多个js文件中存在多个相同名字的function，在调用此function是也会造成冲突。  
  
除了**全局作用域下容易造成变量冲突**这种弊端之外，还有：   
- 当js文件之间存在依赖关系时，必须严格保证加载顺序，被依赖的文件要放在前面。   
- 加载时，浏览器会停止渲染（同步加载），加载文件越多，浏览器响应的时间越长。  
   


**注**： 这里用到了ES6模板文本特性，我用的chrome,说明chrome是支持ES6的，但不是全部都支持，比如export和import.


## 使用RequireJS加载模块    

RequireJS是基于AMD规范实现的，在学习RequireJS之前，先介绍一下CommonJS和AMD这两种模块规范。    
### CommonJS规范

CommonJS是为了解决javascript的作用域问题而定义的模块形式，使每个模块可以在它自身的作用域中执行。规范的主要内容是：必须通过module.exports暴露对外的变量或接口；通过require()方法导入其他模块的的输出到当前模块，并且require模块是同步加载。    

用于在服务端解析javacript的[Nodejs](https://nodejs.org/en/)就是参照CommonJS规范实现的，在浏览器端也有对CommonJS模块规范的实现，比如[Browserify](http://browserify.org/)。但同步加载  模块的方式不适合在浏览器环境中,因为需要等待所有模块都加载完成才开始渲染页面，响应时间很长。

因而，浏览器端的模块只能采用异步加载，这才有了AMD规范的诞生。

### AMD规范

AMD(异步模块定义)规范支持在浏览器环境中异步加载模块。[RequireJS](http://requirejs.org/)正是AMD规范的一种实现。 

**AMD规范的的核心思想**：模块的加载不影响在它之后的程序的执行，所有依赖这个模块的语句都定义在一个回调函数中，模块加载完成后，再运行回调函数。

### RequireJS

RequireJS定义了三个变量：requirejs, require, define.其中requirejs === require

**模块的定义**：通过**define函数**定义在闭包中，define函数格式为： 
   
	define(String id,String[] dependencies,Function|Object factory)
- id:模块的名字，可选参数
- dependencies： 自身依赖的模块列表，这些依赖模块的输出作为factory的参数。可选参数。如果没有指定，则默认的依赖模块为['require','exports','module']
- factory: 一个函数或者对象。若是一个函数，该函数的返回值就是模块的输出。
   
**模块的加载**：require方法用来加载依赖模块，并执行加载完之后的逻辑，require定义格式为：    
  
	require(String[] dependencies,Function callback)


下面来画个页面，用RequireJS来解决模块的定义、依赖和加载问题。目录结构如下：

```
	.
	├── entry.js
	├── index.html
	├── lib
	│   └── require.js
	└── module
	    ├── module1.js
	    └── module2.js
```
	    
要使用define和require函数定义和加载模块需要引入require.js文件。    
从[RequireJS 官网](http://requirejs.org/docs/download.html)下载最新版本，放在lib文件下。


其中module1和module2是打印文件名的两个模块，定义如下：  
module1.js  

	define(function(){
		function printModule1FileName(){
			document.write('This is module1<br/>');
		}
		return {
			filename:"module1",
			printModule1FileName:printModule1FileName
		};
	});

module2.js     

	define(function(){
		var printModule2FileName = function(){
			document.write('This is module2<br/>');
		}
		return {
			filename:"module2",
			printModule2FileName:printModule2FileName
		};
	});

在entry.js中导入模块并调用依赖模块中的方法打印文件名  

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
在index.html中引入require.js和entry.js    

	<!DOCTYPE html>
	<html>
		<head>
			<title>RequireJS</title>
			<meta charset="utf-8">
		</head>
		<body>
			<script src="lib/require.js"></script>   
			<script src="entry.js"></script>
			</body>
	</html>
	
chrome浏览器打开index.html,显示   

	module1 and module2
	This is module1
	This is module2
	
RequireJS解决了并行加载模块的问题，但因为还有以下弊端    
- 代码书写不太符合常规习惯，模块的引入与代码逻辑掺杂在一起。    
- 只能将js文件作为模块加载。    
- 一个加载依赖多个模块就要并行发出多次HTTP请求，影响网页加载速度。虽然require.js提供了一个优化工具可以将多个模块合并在一个文件中只发出一次请求，但一次加载所有模块也很浪费流量，初始化过程慢。
	
关于RequireJS可参考：
   
- [RequireJS 官网](http://requirejs.org/)   
- [RequireJS 中文网](http://www.requirejs.cn/)   
- [RequireJS GitHub](https://github.com/requirejs) 这里有很多RequireJS使用demo

## webpack模块管理
webpack可以做到按需异步加载所有资源，除了js文件，图片、样式文件、html、模板库等都可以视作模块加载。当然，webpack本身只能加载js文件，需要借助Loader转换器来完成。

在用RequireJS加载非AMD规范模块前，需要先用require.config()方法定义shim属性，标注依赖模块的输出值和依赖性。webpack则只需要直接require,不管模块形式是CommonJS、AM还是普通的JS文件。

在webpack-demo文件目录下新建文件目录如下：   

	.
	├── entry.js
	├── index.html
	└── module
	    ├── module1.js
	    └── module2.js

index.html    

	<!DOCTYPE html>
	<html>
		<head>
			<title>webpack</title>
			<meta charset="utf-8">
		</head>
		<body>
			<script src="./dist/bundle.js"></script>
		</body>
	</html>
entry.js    

	var printModule1 = require('./module/module1.js')
	var printModule2 = require('./module/module2.js')
	
	function printEntryFileName(){	
		printModule1();
		printModule2();
	}
	
	printEntryFileName();
	
module1.js    

	var filename = "module1";
	function printModule1FileName(){
		document.write(`This is ${filename}<br/>`);
	}
	
	module.exports = printModule1FileName;
	
html中引入的是bundle.js文件，这个文件是从哪里来的呢？    
在得到这个文件之前需要先一次安装nodejs和webpack,node安装和升级参见：xxx    
全局安装webpack    `npm install webpack -g`    
在webpack目录下执行  `webpack entry.js ./dist.bundle.js`,命令行会输出如下信息： 
   
	Hash: 71d45b6722deab7e60ec
	Version: webpack 1.13.2
	Time: 75ms
	        Asset              Size     Chunks  Chunk      Names
	        bundle.js          2 kB       0     [emitted]  main
	   [0] ./entry.js          192 bytes {0}    [built]
	   [1] ./module/module1.js 147 bytes {0}    [built]
	   [2] ./module/module2.js 145 bytes {0}    [built]
	   
打开index.html文件可以看到输出   
   
	This is module1    
	This is module2
这里涉及到了webpack几个重要的概念：入口(entry)、模块(module)、分块(chunk)
Asset这一列是webpack编译输出的文件，bundle.js是入口，在html中通过script标签引入，其他的是被entry依赖的模块，webpack将他们按照依赖顺序进行编号.

若要给输出加一个样式，需要在package.json文件中加入loader转换器依赖。   
   
	npm init    
	npm install css-loader style-loader
	
然后在entry.js中引入css模块，然后重新编译  
  
	require("!style!css!./style/style.css")
	var printModule1 = require('./module/module1.js')
	var printModule2 = require('./module/module2.js')
	
	function printEntryFileName(){	
		printModule1();
		printModule2();
	}
	
	printEntryFileName();
	
webpack还可以根据配置文件编译打包，不需要在webpack命令后写参数（输入文件和输出文件），    
默认情况下会搜索当前目录下webpack.config.js文件，也可以通过  --config来指定配置文件。在webpack-demo文件目录下新增webpack.config.js    
var webpack = require('webpack');

	module.exports = {
		entry:'./entry.js',
		output: {
			path: __dirname + '/dist',
			filename: 'bundle.js'
		},
		module: {
			loaders:[
				{
					test:/\.css$/,loader:'style!css'
				}
			]
	
		}
	}
	
在终端运行webpack,会得到跟webpack entry.js ./dist.bundle.js一样的结果。最后的文件目录是这样子的    

	.
	├── dist
	│   └── bundle.js
	├── entry.js
	├── index.html
	├── module
	│   ├── module1.js
	│   └── module2.js
	├── package.json
	├── style
	│   └── style.css
	└── webpack.config.js

## 当webpack遇见ES6
把上面最后的文件目录copy出来放到webpack-es6文件目录中，进入webpack-es6目录下操作。在项目中使用ES6语法需要做一些配置。
### .babelrc文件
在webpack-demo目录下新建.babelrc，这个是将ES6转码成ES5必须要有的文件。文件中的配置如下：    

	{
	  "presets": ["es2015"],
	  "plugins": []
	}
.babelrc文件的配置可以参考[Babel 入门教程](http://www.ruanyifeng.com/blog/2016/01/babel.html)，如果项目使用的React+ES6，presets中则加入 "react"。
### 配置webpack.config.js
在loaders中加入babel配置

	var webpack = require('webpack');
	module.exports = {
		entry:'./entry.js',
		output: {
			path: __dirname + '/dist',
			filename: 'bundle.js'
		},
		module: {
			loaders:[
				{
					test:/\.css$/,loader:'style!css'
				},
				{
	              test: /\.js$/,
	              loader: 'babel',
	              exclude: /node_modules/
	            }
			]
		}
	}

这里既然用到了npm install babel babel-loader --save
### 安装必须的依赖
.babelrc这个配置加了之后需要安装规则集    
	
	ES2015转码规则
	npm install babel-preset-es2015 --save
	
	react转码规则
	npm install babel-preset-react --save
webpack.config.js中加入babel-loader也需要安装，还有最基本的bable模块  

	npm install babel babel-loader --save
### 使用ES6语法改造项目
module1.js 与 module2.js    

	export function printModule1FileName(filename){
		document.getElementById('module1').innerHTML = `This is ${filename}`
	}

entry.js    

	import "!style!css!./style/style.css"
	import * as Module1 from './module/module1.js'
	import * as Module2 from './module/module2.js'
	
	class Entry{
		constructor(filename1,filename2){
			Module1.printModule1FileName(filename1);
			Module2.printModule2FileName(filename2);
		}
	}
	
	(() => new Entry("es6module1","es6module1"))();

或者这样的entry.js    

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
其他的文件内容不变，在webpack-es6目录下执行webpack输出内容：    

	This is es6module1
	This is es6module1

最后的目录    

	.
	├── .babelrc
	├── dist
	│   └── bundle.js
	├── entry.js
	├── index.html
	├── module
	│   ├── module1.js
	│   └── module2.js
	├── package.json
	├── style
	│   └── style.css
	└── webpack.config.js
	

关于ES6可参考：  

- [ECMAScript 6入门](http://es6.ruanyifeng.com/)    
- [Babel 入门教程](http://www.ruanyifeng.com/blog/2016/01/babel.html)    
- [检测浏览器支持的ES6特性](http://ruanyf.github.io/es-checker/index.cn.html)
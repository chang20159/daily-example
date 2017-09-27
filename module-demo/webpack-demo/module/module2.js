var filename = "module2";
function printModule2FileName(){
	document.getElementById('module2').innerHTML = `This is ${filename}`
}

module.exports = printModule2FileName;
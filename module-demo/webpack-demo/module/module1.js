var filename = "module1";
function printModule1FileName(){
	document.getElementById('module1').innerHTML = `This is ${filename}`
}

module.exports = printModule1FileName;


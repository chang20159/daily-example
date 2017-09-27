var Request = class Request{
    constructor(){
        this.method = 'GET'
    }

    getXMLHttpRequest(){
        let xhr = null;
        try{
            xhr = new XMLHttpRequest();
        }catch(e1){
            try{
                //IE5和IE6
                xhr = new ActiveXObject("microsoft.xmlhttp");
            }catch(e2){
                window.alert("您的浏览器不支持Ajax,换一个浏览器试试吧！");
            }
        }
        return xhr;
    }
    
    send(url,opts={}){
        let xhr = this.getXMLHttpRequest();
        xhr.open(opts.method || this.method,url,opts.asyn || true);
        xhr.withCredentials = opts.withCredentials;
        xhr.onreadystatechange = function(){
            switch(xhr.readyState){
                case 0 : 
                    console.log("请求未初始化，uninitialized....");
                    break;
                case 1 :
                    console.log("服务器连接已建立，loading....");
                    break;
                case 2 :
                    console.log("请求已接收，loaded....");
                    break;
                case 3 :
                    console.log("请求处理中，interactive....");
                    break;
                case 4 :
                    console.log("请求已完成，且响应已就绪，complete....");
                    if(xhr.status >=200 && xhr.status < 300 || xhr == 304){
                        console.log(xhr.responseText);
                        opts.success && opts.success.call(xhr,xhr.responseText)
                    }else{
                        console.log(`status: ${xhr.status}，请求出错了，再试一下看看~~`);
                    }
                    break;
            }
        }
        opts.header = opts.header || {};
        for (var key in opts.header) {
            xhr.setRequestHeader(key, opts.header[key]);
        }
        xhr.send(opts.body);
        return this;
    }
}




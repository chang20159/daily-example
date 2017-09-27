package link.groovy.net

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import utils.BasicAuthUtil

/**
 * Created by chang on 16/3/27.
 */

def http = new HTTPBuilder("http://wiki.example.com")

/**
 * request方法三个参数
 * 1. 请求方法
 * 2. ContentType   默认 ContentType.ANY   按ContentType解析响应内容
 * 3. 请求配置的闭包
 */
http.request(Method.GET,ContentType.JSON) { req ->
    uri.path = '/rest/api/content/search'
    uri.query = [cql,'space=\"~yanfang.chang\" and label in (\"热门\",\"团购\"']
    headers.'Authorization' = BasicAuthUtil.basicAuth

    //设置成功响应的处理闭包
    response.success = { resp,reader ->
        assert resp.status == 200
        println "My response handler got response: ${resp.statusLine}"
        println "Response length: ${resp.headers.'Content-Length'}"
        System.out << reader

    }

    //根据响应状态码分别指定处理闭包
    response.'404' = { println 'not found' }
    //未根据响应码指定的失败处理闭包
    response.failure = { println "Unexpected failure: ${resp.statusLine}" }
}
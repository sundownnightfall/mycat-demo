package com.studies.cat;

import java.io.InputStream;

/**
 * @author： yangh
 * 请求对象 ：是对Socket.InputStream的处理
 * @date： Created on 2020/6/2 8:53
 * @version： v1.0
 * @modified By:
 */

public class Request {
    private InputStream inputStream;

    private String uri;
    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }
    //    GET /index.html HTTP/1.1 \r\n
    //    Host: 127.0.0.1:8080
    //    Connection: keep-alive
    //    Cache-Control: max-age=0
    //    Upgrade-Insecure-Requests: 1
    //    User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36
    //    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
    //    Sec-Fetch-Site: cross-site
    //    Sec-Fetch-Mode: navigate
    //    Sec-Fetch-User: ?1
    //    Sec-Fetch-Dest: document
    //    Accept-Encoding: gzip, deflate, br
    //    Accept-Language: zh-CN,zh;q=0.9,en;q=0.8
    public void parse() throws Exception{
        //解析HTTP协议
        byte[] bytes = new byte[1024];
        //将数据读入到byte数组中
        inputStream.read(bytes);
        String string = new String(bytes);
        uri = parseUri(string);

    }
    public String getUri(){
        return uri;
    }
    private String parseUri(String httpRequestString){
        //Get index.html 200  协议形式
        int indexStart = httpRequestString.indexOf(" ");
        if(indexStart!=-1){
            int indexEnd = httpRequestString.indexOf(" ",indexStart+1);
            if(indexEnd>indexStart){
                return httpRequestString.substring(indexStart+1,indexEnd);
            }
        }
        return null;
    }
}

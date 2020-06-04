package com.studies.cat.processor;

import com.studies.cat.Request;
import com.studies.cat.Response;
import com.studies.cat.connector.HttpRequest;
import com.studies.cat.connector.HttpResponse;

/**
 * @author： yangh
 * @date： Created on 2020/6/2 15:08
 * @version： v1.0
 * @modified By:
 */
//静态资源请求处理器
public class HttpStaticResourceProcessor {
    //静态资源处理逻辑
    public void process(HttpRequest request, HttpResponse response){
        try{
            //静态文件，直接返回
            //response.sendStaticData();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

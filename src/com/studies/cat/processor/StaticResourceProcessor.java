package com.studies.cat.processor;

import com.studies.cat.Request;
import com.studies.cat.Response;

/**
 * @author： yangh
 * @date： Created on 2020/6/2 15:08
 * @version： v1.0
 * @modified By:
 */
//静态资源请求处理器
public class StaticResourceProcessor {
    //静态资源处理逻辑
    public void process(Request request, Response response){
        try{
            //静态文件，直接返回
            response.sendStaticData();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

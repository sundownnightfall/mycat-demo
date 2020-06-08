package com.studies.cat.start;

import com.studies.cat.connector.HttpConnector;

/**
 * @author： yangh
 * @date： Created on 2020/6/3 10:38
 * @version： v1.0
 * @modified By:
 */
//启动类
public class Bootstrap {
    public static void main(String[] args){
        System.out.println("开始启动服务器");
        HttpConnector httpConnector = new HttpConnector();
        httpConnector.start();
    }
}

package com.wgh.nacos3.listener.nacos;

import com.alibaba.cloud.nacos.annotation.NacosConfigListener;
import org.springframework.stereotype.Service;

@Service
public class MyRateConfigService {

    @NacosConfigListener(dataId = "routeconfig",group = "config")
    public void rate(String rateConfig) {
        System.out.println("receiveRateConfig:"+rateConfig);
    }

}
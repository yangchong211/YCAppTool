package com.yc.businessinterface;


import com.yc.spi.annotation.ServiceProviderInterface;

@ServiceProviderInterface
public interface IAnimServiceProvider {

    void setStartAnim();

    void setAnimTime(int time);

    int getAnimTime();
}

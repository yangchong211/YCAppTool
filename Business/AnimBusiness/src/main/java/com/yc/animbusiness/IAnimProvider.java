package com.yc.animbusiness;


import com.yc.spi.annotation.ServiceProviderInterface;

//@ServiceProviderInterface
public interface IAnimProvider {

    void setStartAnim();

    void setAnimTime(int time);

    int getAnimTime();
}

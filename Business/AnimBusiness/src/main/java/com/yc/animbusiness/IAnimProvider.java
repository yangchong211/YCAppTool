package com.yc.animbusiness;

import com.yc.api.getIt.ServiceProviderInterface;

@ServiceProviderInterface
public interface IAnimProvider {

    void setStartAnim();

    void setAnimTime(int time);

    int getAnimTime();
}

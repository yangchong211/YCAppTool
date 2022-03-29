package com.yc.businessinterface;

import com.yc.spi.annotation.ServiceProviderInterface;

@ServiceProviderInterface
public interface AppServiceProvider {

    void toast();

    void dialog();

    void snackBar();

}

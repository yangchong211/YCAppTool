package com.yc.blesample.ble.comm;



import com.yc.easyble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager implements IObservable {

    public static ObserverManager getInstance() {
        return ObserverManagerHolder.sObserverManager;
    }

    private static class ObserverManagerHolder {
        private static final ObserverManager sObserverManager = new ObserverManager();
    }

    private final List<IObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(IObserver obj) {
        observers.add(obj);
    }

    @Override
    public void deleteObserver(IObserver obj) {
        int i = observers.indexOf(obj);
        if (i >= 0) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObserver(BleDevice bleDevice) {
        for (int i = 0; i < observers.size(); i++) {
            IObserver o = observers.get(i);
            o.disConnected(bleDevice);
        }
    }

}

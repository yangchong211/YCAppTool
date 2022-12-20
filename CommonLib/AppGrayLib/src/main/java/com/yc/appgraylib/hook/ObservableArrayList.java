package com.yc.appgraylib.hook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 具有感知的ArrayList
 *     revise :
 * </pre>
 */
public class ObservableArrayList<T> extends ArrayList<T> {

    private final List<OnListChangeListener> mListeners = new ArrayList<>();

    public void addOnListChangedListener(OnListChangeListener listener) {
        if (mListeners != null) {
            mListeners.add(listener);
        }
    }

    public void removeOnListChangedListener(OnListChangeListener listener) {
        if (mListeners != null) {
            mListeners.remove(listener);
        }
    }

    @Override
    public boolean add(T object) {
        super.add(object);
        notifyAdd(size() - 1, 1);
        return true;
    }

    @Override
    public void add(int index, T object) {
        super.add(index, object);
        notifyAdd(index, 1);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        int oldSize = size();
        boolean added = super.addAll(collection);
        if (added) {
            notifyAdd(oldSize, size() - oldSize);
        }
        return added;
    }


    @Override
    public void clear() {
        int oldSize = size();
        super.clear();
        if (oldSize != 0) {
            notifyRemove(0, oldSize);
        }
    }

    @Override
    public T remove(int index) {
        T val = super.remove(index);
        notifyRemove(index, 1);
        return val;
    }

    @Override
    public boolean remove(Object object) {
        int index = indexOf(object);
        if (index >= 0) {
            remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public T set(int index, T object) {
        T val = super.set(index, object);
        if (mListeners != null) {
            for (OnListChangeListener listener : mListeners) {
                listener.onChange(this, index, 1);
            }
        }
        return val;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notifyRemove(fromIndex, toIndex - fromIndex);
    }

    private void notifyAdd(int start, int count) {
        if (mListeners != null) {
            for (OnListChangeListener listener : mListeners) {
                listener.onAdd(this, start, count);
            }
        }
    }

    private void notifyRemove(int start, int count) {
        if (mListeners != null) {
            for (OnListChangeListener listener : mListeners) {
                listener.onRemove(this, start, count);
            }
        }
    }

    public interface OnListChangeListener {

        void onChange(ArrayList list, int index, int count);

        void onAdd(ArrayList list, int start, int count);

        void onRemove(ArrayList list, int start, int count);
    }
}

package com.ns.yc.lifehelper.ui.other.myPicture.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


public abstract class ArrayRecyclerAdapter<E, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements List<E> {

    private final Object lock = new Object();

    private final List<E> list;

    @SuppressWarnings("unused")
    public ArrayRecyclerAdapter() {
        list = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public ArrayRecyclerAdapter(int capacity) {
        list = new ArrayList<>(capacity);
    }

    @SuppressWarnings("unused")
    public ArrayRecyclerAdapter(Collection<? extends E> collection) {
        list = new ArrayList<>(collection);
    }

    @Override
    public int getItemCount() {
        return size();
    }

    @Override
    public void add(int location, E object) {
        synchronized (lock) {
            list.add(location, object);
            notifyItemInserted(location);
        }
    }

    @Override
    public boolean add(E object) {
        synchronized (lock) {
            int lastIndex = list.size();
            if (list.add(object)) {
                notifyItemInserted(lastIndex);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean addAll(int location, @NonNull Collection<? extends E> collection) {
        synchronized (lock) {
            if (list.addAll(location, collection)) {
                notifyItemRangeInserted(location, collection.size());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends E> collection) {
        synchronized (lock) {
            int lastIndex = list.size();
            if (list.addAll(collection)) {
                notifyItemRangeInserted(lastIndex, collection.size());
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            int size = list.size();
            if (size > 0) {
                list.clear();
                notifyItemRangeRemoved(0, size);
            }
        }
    }

    @Override
    public boolean contains(Object object) {
        return list.contains(object);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return list.containsAll(collection);
    }

    @Override
    public E get(int location) {
        return list.get(location);
    }

    @Override
    public int indexOf(Object object) {
        return list.indexOf(object);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public int lastIndexOf(Object object) {
        return list.lastIndexOf(object);
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<E> listIterator(int location) {
        return list.listIterator(location);
    }

    @Override
    public E remove(int location) {
        synchronized (lock) {
            E item = list.remove(location);
            notifyItemRemoved(location);
            return item;
        }
    }

    @Override
    public boolean remove(Object object) {
        synchronized (lock) {
            int index = indexOf(object);
            if (list.remove(object)) {
                notifyItemRemoved(index);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        boolean modified = false;

        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (collection.contains(object)) {
                synchronized (lock) {
                    int index = indexOf(object);
                    iterator.remove();
                    notifyItemRemoved(index);
                }

                modified = true;
            }
        }

        return modified;
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        boolean modified = false;

        Iterator<E> iterator = list.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (!collection.contains(object)) {
                synchronized (lock) {
                    int index = indexOf(object);
                    iterator.remove();
                    notifyItemRemoved(index);
                }

                modified = true;
            }
        }

        return modified;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public E set(int location, E object) {
        synchronized (lock) {
            E origin = list.set(location, object);
            if (!Objects.equals(object, origin)) {
                notifyItemChanged(location);
            }
            return origin;
        }
    }

    @Override
    public int size() {
        return list.size();
    }

    @NonNull
    @Override
    public List<E> subList(int start, int end) {
        return list.subList(start, end);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NonNull
    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T[] toArray(@NonNull T[] array) {
        return list.toArray(array);
    }

    public void replaceWith(List<E> data) {
        if (list.isEmpty() && data.isEmpty()) {
            return;
        }

        if (list.isEmpty()) {
            addAll(data);
            return;
        }

        if (data.isEmpty()) {
            clear();
            return;
        }

        if (list.equals(data)) {
            return;
        }

        // 首先将旧列表有、新列表没有的从旧列表去除
        retainAll(data);

        // 如果列表被完全清空了，那就直接全部插入好了
        if (list.isEmpty()) {
            addAll(data);
            return;
        }

        // 然后遍历新列表，对旧列表的数据更新、移动、增加
        for (int indexNew = 0; indexNew < data.size(); indexNew++) {
            E item = data.get(indexNew);

            int indexOld = indexOf(item);

            if (indexOld == -1) {
                add(indexNew, item);
            } else if (indexOld == indexNew) {
                set(indexNew, item);
            } else {
                list.remove(indexOld);
                list.add(indexNew, item);
                notifyItemMoved(indexOld, indexNew);
            }
        }
    }

}


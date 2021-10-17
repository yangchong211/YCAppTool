package com.ycbjie.zoomimagelib.pool;


import java.util.LinkedList;
import java.util.Queue;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 对象池抽象类
 *     revise:
 * </pre>
 */
public abstract class ObjectsPool<T> {


    /*
     * 防止频繁new对象产生内存抖动.
     * 由于对象池最大长度限制,如果吞度量超过对象池容量,仍然会发生抖动.
     * 此时需要增大对象池容量,但是会占用更多内存.
     * <T> 对象池容纳的对象类型
     */

    /**
     * 对象池的最大容量
     */
    private int mSize;

    /**
     * 对象池队列
     */
    private Queue<T> mQueue;

    /**
     * 创建一个对象池
     *
     * @param size 对象池最大容量
     */
    public ObjectsPool(int size) {
        mSize = size;
        mQueue = new LinkedList<>();
    }

    /**
     * 获取一个空闲的对象
     *
     * 如果对象池为空,则对象池自己会new一个返回.
     * 如果对象池内有对象,则取一个已存在的返回.
     * take出来的对象用完要记得调用given归还.
     * 如果不归还,让然会发生内存抖动,但不会引起泄漏.
     *
     * @return 可用的对象
     *
     * @see #given(Object)
     */
    public T take() {
        //如果池内为空就创建一个
        if (mQueue.size() == 0) {
            return newInstance();
        } else {
            //对象池里有就从顶端拿出来一个返回
            return resetInstance(mQueue.poll());
        }
    }

    /**
     * 归还对象池内申请的对象
     * 如果归还的对象数量超过对象池容量,那么归还的对象就会被丢弃
     *
     * @param obj 归还的对象
     *
     * @see #take()
     */
    public void given(T obj) {
        //如果对象池还有空位子就归还对象
        if (obj != null && mQueue.size() < mSize) {
            mQueue.offer(obj);
        }
    }

    /**
     * 实例化对象
     *
     * @return 创建的对象
     */
    abstract protected T newInstance();

    /**
     * 重置对象
     *
     * 把对象数据清空到就像刚创建的一样.
     *
     * @param obj 需要被重置的对象
     * @return 被重置之后的对象
     */
    abstract protected T resetInstance(T obj);

}

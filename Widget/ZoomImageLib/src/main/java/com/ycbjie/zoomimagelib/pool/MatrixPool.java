package com.ycbjie.zoomimagelib.pool;


import android.graphics.Matrix;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 矩阵对象池
 *     revise:
 * </pre>
 */
public class MatrixPool extends ObjectsPool<Matrix>{

    /**
     * 矩阵对象池
     */
    public MatrixPool(int size) {
        super(size);
    }

    @Override
    protected Matrix newInstance() {
        return new Matrix();
    }

    @Override
    protected Matrix resetInstance(Matrix obj) {
        obj.reset();
        return obj;
    }
}

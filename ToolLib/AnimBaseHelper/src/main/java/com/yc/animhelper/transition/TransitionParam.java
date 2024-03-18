package com.yc.animhelper.transition;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * <pre>
 *     @author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/8/11
 *     desc   : 参数值
 *     revise :
 * </pre>
 */
public class TransitionParam implements Parcelable {

    public int width;
    public int height;
    public int left;
    public int right;
    public int top;
    public int bottom;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.left);
        dest.writeInt(this.right);
        dest.writeInt(this.top);
        dest.writeInt(this.bottom);
    }

    public TransitionParam() {
    }

    protected TransitionParam(Parcel in) {
        this.width = in.readInt();
        this.height = in.readInt();
        this.left = in.readInt();
        this.right = in.readInt();
        this.top = in.readInt();
        this.bottom = in.readInt();
    }

    public static final Creator<TransitionParam> CREATOR = new Creator<TransitionParam>() {
        @Override
        public TransitionParam createFromParcel(Parcel source) {
            return new TransitionParam(source);
        }

        @Override
        public TransitionParam[] newArray(int size) {
            return new TransitionParam[size];
        }
    };

}

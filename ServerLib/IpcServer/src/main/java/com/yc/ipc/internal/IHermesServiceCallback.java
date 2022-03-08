

package com.yc.ipc.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import java.util.List;

public interface IHermesServiceCallback extends IInterface {

    abstract class Stub extends Binder implements IHermesServiceCallback {

        private static final String DESCRIPTOR = "com.yc.ipc.internal.IHermesServiceCallback";

        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        public static IHermesServiceCallback asInterface(IBinder obj) {
            if ((obj==null)) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin!=null)&&(iin instanceof IHermesServiceCallback))) {
                return ((IHermesServiceCallback)iin);
            }
            return new Proxy(obj);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION:
                    reply.writeString(DESCRIPTOR);
                    return true;
                case TRANSACTION_callback:
                    data.enforceInterface(DESCRIPTOR);
                    CallbackMail _arg0;
                    if ((0!=data.readInt())) {
                        _arg0 = CallbackMail.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Reply _result = this.callback(_arg0);
                    reply.writeNoException();
                    if ((_result!=null)) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case TRANSACTION_gc:
                    data.enforceInterface(DESCRIPTOR);
                    List list1, list2;
                    ClassLoader cl = this.getClass().getClassLoader();
                    list1 = data.readArrayList(cl);
                    list2 = data.readArrayList(cl);
                    this.gc(list1, list2);
                    reply.writeNoException();
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements IHermesServiceCallback {

            private IBinder mRemote;

            Proxy(IBinder remote) {
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            public String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public Reply callback(CallbackMail mail) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Reply _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((mail!=null)) {
                        _data.writeInt(1);
                        mail.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_callback, _data, _reply, 0);
                    _reply.readException();
                    if ((0!=_reply.readInt())) {
                        _result = Reply.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void gc(List<Long> timeStamps, List<Integer> indexes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeList(timeStamps);
                    _data.writeList(indexes);
                    mRemote.transact(Stub.TRANSACTION_gc, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_callback = IBinder.FIRST_CALL_TRANSACTION;

        static final int TRANSACTION_gc = IBinder.FIRST_CALL_TRANSACTION + 1;
    }

    Reply callback(CallbackMail mail) throws RemoteException;

    /**
     * http://business.nasdaq.com/marketinsite/2016/Indexes-or-Indices-Whats-the-deal.html
     *
     * This article says something about the plural form of "index".
     */
    void gc(List<Long> timeStamps, List<Integer> indexes) throws RemoteException;

}
package com.cymjoe.aidldemo2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.cymjoe.aidldemo.IBean;
import com.cymjoe.aidldemo.TestBean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestService extends Service {
    private CopyOnWriteArrayList<TestBean> list = new CopyOnWriteArrayList<>();

    public TestService() {
    }

    Binder binder = new IBean.Stub() {

        @Override
        public List<TestBean> getAll() throws RemoteException {
            return list;
        }

        @Override
        public void add(TestBean bean) throws RemoteException {
            String name = bean.getName();
            bean.setName(name + "from AIDL");
            list.add(bean);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

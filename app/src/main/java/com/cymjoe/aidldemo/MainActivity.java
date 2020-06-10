package com.cymjoe.aidldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    IBean bean;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        bindService();
    }

    IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (bean == null) {
                return;
            }
            bean.asBinder().unlinkToDeath(deathRecipient, 0);
            bean = null;
            bindService();
        }
    };

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bean = IBean.Stub.asInterface(service);
            refresh();
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void refresh() {
        try {
            tv.setText(bean.getAll().toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void bindService() {
        Intent intent = new Intent("com.cymjoe.aidldemo2.TestService");
        intent.setPackage("com.cymjoe.aidldemo2");
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    public void add(View view) {
        try {
            bean.add(new TestBean("addData："));
            refresh();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

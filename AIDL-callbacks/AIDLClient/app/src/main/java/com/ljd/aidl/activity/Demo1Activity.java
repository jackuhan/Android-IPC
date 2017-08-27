package com.ljd.aidl.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ljd.aidl.ICalculate;
import com.ljd.aidl.client.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Demo1Activity extends AppCompatActivity {

    private final String TAG = "DEMO1";
    //是否已经绑定service
    private boolean mIsBindService;
    private ICalculate mCalculate;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"bind success");
            Toast.makeText(Demo1Activity.this,"bind service success",Toast.LENGTH_SHORT).show();
            mCalculate = ICalculate.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //重新绑定Service防止系统将服务进程杀死而产生的调用错误。
            bindService();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        ButterKnife.bind(this);
        mIsBindService = false;
    }

    @Override
    protected void onDestroy() {
        unbindService();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick({ R.id.bind_demo1_btn,R.id.unbind_demo1_btn,R.id.calculate_btn})
    public void onClickButton(View v) {
        switch (v.getId()){
            case R.id.bind_demo1_btn:
                bindService();
                break;
            case R.id.unbind_demo1_btn:
                Toast.makeText(this,"unbind service success",Toast.LENGTH_SHORT).show();
                unbindService();
                break;
            case R.id.calculate_btn:
                if (mIsBindService && mCalculate != null ){
                    try {
                        int result = mCalculate.add(2,4);
                        Log.d(TAG,String.valueOf(result));
                        Toast.makeText(this,String.valueOf(result),Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this,"not bind service",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void bindService(){
        Intent intent = new Intent();
        intent.setAction("com.ljd.aidl.action.CALCULATE_SERVICE");
        intent.setPackage("com.ljd.aidl.server");//这里你需要设置你应用的包名
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        mIsBindService = true;
    }

    private void unbindService(){
        if(mIsBindService){
            mIsBindService = false;
            unbindService(mConnection);
        }
    }
}

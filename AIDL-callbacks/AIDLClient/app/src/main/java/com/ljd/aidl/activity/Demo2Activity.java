package com.ljd.aidl.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljd.aidl.IComputerManager;
import com.ljd.aidl.client.R;
import com.ljd.aidl.entity.ComputerEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Demo2Activity extends AppCompatActivity{

    @Bind(R.id.show_linear)
    LinearLayout mShowLinear;

    private boolean mIsBindService;
    private IComputerManager mRemoteComputerManager;
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Toast.makeText(Demo2Activity.this,"binderDied",Toast.LENGTH_SHORT).show();
            if(mRemoteComputerManager != null){
                mRemoteComputerManager.asBinder().unlinkToDeath(mDeathRecipient,0);
                mRemoteComputerManager = null;
                bindService();
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIsBindService = true;
            Toast.makeText(Demo2Activity.this,"bind service success",Toast.LENGTH_SHORT).show();
            mRemoteComputerManager = IComputerManager.Stub.asInterface(service);
            try {
                mRemoteComputerManager.asBinder().linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteComputerManager = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);
        ButterKnife.bind(this);
        mIsBindService = false;
    }

    @Override
    protected void onDestroy() {
        unbindService();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick({R.id.bind_demo2_btn,R.id.unbind_demo2_btn,R.id.test_demo2_btn,R.id.clear_demo2_btn})
    public void onClickButton(View v) {
        switch (v.getId()){
            case R.id.bind_demo2_btn:
                bindService();
                break;
            case R.id.unbind_demo2_btn:
                Toast.makeText(this,"unbind service success",Toast.LENGTH_SHORT).show();
                unbindService();
                break;
            case R.id.test_demo2_btn:
                if (!mIsBindService || mRemoteComputerManager == null){
                    Toast.makeText(this,"not bind service",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    List<ComputerEntity> computerList = mRemoteComputerManager.getComputerList();
                    for (int i =0;i<computerList.size();i++){
                        String str = "computerId:" + String.valueOf(computerList.get(i).computerId) +
                                " brand:" + computerList.get(i).brand +
                                " model:" + computerList.get(i).model ;
                        TextView textView = new TextView(this);
                        textView.setText(str);
                        mShowLinear.addView(textView);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.clear_demo2_btn:
                mShowLinear.removeAllViews();
                break;
        }
    }

    private void bindService(){
        Intent intent = new Intent();
        intent.setAction("com.ljd.aidl.action.COMPUTER_SERVICE");
        intent.setPackage("com.ljd.aidl.server");//这里你需要设置你应用的包名
        mIsBindService = bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        if(!mIsBindService){
            return;
        }
        mIsBindService = false;
        unbindService(mConnection);
    }
}

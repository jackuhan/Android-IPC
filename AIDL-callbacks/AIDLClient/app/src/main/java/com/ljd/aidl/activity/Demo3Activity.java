package com.ljd.aidl.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljd.aidl.IComputerManagerObserver;
import com.ljd.aidl.IOnComputerArrivedListener;
import com.ljd.aidl.client.R;
import com.ljd.aidl.entity.ComputerEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Demo3Activity extends AppCompatActivity {

    @Bind(R.id.show_demo3_linear)
    LinearLayout mShowLinear;

    private boolean mIsBindService;
    private static final int MESSAGE_COMPUTER_ARRIVED = 1;

    private IComputerManagerObserver mRemoteComputerManager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_COMPUTER_ARRIVED:
                    ComputerEntity computer = (ComputerEntity)msg.obj;
                    String str = "computerId:" + String.valueOf(computer.computerId) +
                            " brand:" + computer.brand +
                            " model:" + computer.model ;
                    TextView textView = new TextView(Demo3Activity.this);
                    textView.setText(str);
                    mShowLinear.addView(textView);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
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
            Toast.makeText(Demo3Activity.this,"bind service success",Toast.LENGTH_SHORT).show();
            mRemoteComputerManager = IComputerManagerObserver.Stub.asInterface(service);
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

    private IOnComputerArrivedListener mOnComputerArrivedListener = new IOnComputerArrivedListener.Stub(){

        @Override
        public void onComputerArrived(ComputerEntity computer) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_COMPUTER_ARRIVED,computer).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);
        ButterKnife.bind(this);
        mIsBindService = false;
    }

    @Override
    protected void onDestroy() {
        unbindService();
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @OnClick({R.id.bind_demo3_btn,R.id.unbind_demo3_btn,R.id.test_demo3_btn,R.id.clear_demo3_btn})
    public void onClickButton(View v){
        switch (v.getId()){
            case R.id.bind_demo3_btn:
                bindService();
                break;
            case R.id.unbind_demo3_btn:
                Toast.makeText(this,"unbind service success",Toast.LENGTH_SHORT).show();
                unbindService();
                break;
            case R.id.test_demo3_btn:
                if (!mIsBindService || mRemoteComputerManager == null){
                    Toast.makeText(this,"not bind service",Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    ComputerEntity computer = new ComputerEntity(3,"hp","envy13");
                    mRemoteComputerManager.addComputer(computer);
                    List<ComputerEntity> computerList = mRemoteComputerManager.getComputerList();
                    for (int i =0;i<computerList.size();i++){
                        String str = "computerId:" + String.valueOf(computerList.get(i).computerId) +
                                " brand:" + computerList.get(i).brand +
                                " model:" + computerList.get(i).model ;
                        TextView textView = new TextView(this);
                        textView.setText(str);
                        mShowLinear.addView(textView);
                    }
                    mRemoteComputerManager.registerUser(mOnComputerArrivedListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.clear_demo3_btn:
                mShowLinear.removeAllViews();
                break;
        }
    }

    private void bindService(){
        Intent intent = new Intent();
        intent.setAction("com.ljd.aidl.action.COMPUTER_OBSERVER_SERVICE");
        intent.setPackage("com.ljd.aidl.server");//这里你需要设置你应用的包名
        mIsBindService = bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(){
        if(!mIsBindService){
            return;
        }
        if (mRemoteComputerManager != null && mRemoteComputerManager.asBinder().isBinderAlive()){
            try {
                mRemoteComputerManager.unRegisterUser(mOnComputerArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        mIsBindService = false;
    }
}

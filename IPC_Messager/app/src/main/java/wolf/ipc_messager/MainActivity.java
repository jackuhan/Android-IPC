package wolf.ipc_messager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import wolf.ipc_messager.server.WolfService;

public class MainActivity extends AppCompatActivity {

    private Button btn1;
    private WolfServiceConnection connection;
    private TextView tv;
    private TextView tv2;
    private Messenger mService;

    class WolfServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            tv2.setText("连接成功");

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            tv2.setText("disconnected!");
        }
    }
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定服务
        Intent  intent = new Intent(MainActivity.this, WolfService.class);
        intent.setAction("wolf.wolf");
        connection = new WolfServiceConnection();
        bindService(intent, connection,  Context.BIND_AUTO_CREATE);

        btn1 = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这里再点击事件中发送消息
                       int a = i++;
                       int b = (int) (Math.random() * 100);
                       tv.setText(" messager: "+a + " + " + b + " =");
                       try {
                           Message msgFromClient = Message.obtain(null, MSG_SUM,a,b);
                           msgFromClient.replyTo = mMessenger;
                           mService.send(msgFromClient);
                       } catch (Exception e) {
                           e.printStackTrace();
                       }
            }
        });
    }
    private static final int MSG_SUM = 0x110;
    private Messenger mMessenger = new Messenger(new Handler()
    {
        @Override
        public void handleMessage(Message msgFromServer)
        {
            switch (msgFromServer.what)
            {
                case MSG_SUM:
                    tv.setText(tv.getText() + "=>" + msgFromServer.arg2);
                    break;
            }
            super.handleMessage(msgFromServer);
        }
    });
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

}

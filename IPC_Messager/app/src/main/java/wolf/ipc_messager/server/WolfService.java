package wolf.ipc_messager.server;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/13.
 */

public class WolfService extends Service {
    private static final int MSG_SUM = 0x110;

    private final static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msgfromClient) {

            Message msgToClient = Message.obtain(msgfromClient);//返回给客户端的消息
            switch (msgfromClient.what) {
                //msg 客户端传来的消息
                case MSG_SUM:
                    msgToClient.what = MSG_SUM;
                    try {
                        //模拟耗时
                        Thread.sleep(2000);
                        msgToClient.arg2 = msgfromClient.arg1 + msgfromClient.arg2;
                        //replyTo是关键代码（把消息返还给客户端）
                        msgfromClient.replyTo.send(msgToClient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            super.handleMessage(msgfromClient);
        }
    };
    /**
     * 把handler传入Messager中
     */
    private final static Messenger messenger = new Messenger(handler);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //返回messager里的bindler
        return messenger.getBinder();
    }
}

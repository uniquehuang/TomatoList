package hwl.bysj.tomatolist.tomatoclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class TimeOutReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        if(null==ClockActivity.mInstance){
            Intent i = new Intent(context,ClockActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("notification",true);
            context.startActivity(i);
        }
        else{
            ClockActivity.mInstance.requireNotification();
        }
    }
}

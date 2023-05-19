package hwl.bysj.tomatolist.util;

import android.app.Application;

/**
 * Created by WorldSkills2020 on 2/23/2020.
 */

public class Program extends Application {

    public static final String ACTION_CLOCK="hwl.bysj.tomatolist.ACTION_CLOCK";

    @Override
    public void onCreate() {
        super.onCreate();
        SpUtils.init(this);
    }
}

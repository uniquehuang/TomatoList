package hwl.bysj.tomatolist.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import hwl.bysj.tomatolist.R;
import hwl.bysj.tomatolist.util.PackageUtil;


public class InfoActivity extends AppCompatActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, InfoActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

    /*    setToolBar();*/

        TextView infoVersion = (TextView)findViewById(R.id.info_version);
        infoVersion.setText(getResources().getString(R.string.info_version,
                getResources().getString(R.string.app_name), PackageUtil.versionName(this)));
    }

 /*   private void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

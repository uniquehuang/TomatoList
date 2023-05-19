package hwl.bysj.tomatolist;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import hwl.bysj.tomatolist.about.InfoActivity;
import hwl.bysj.tomatolist.base.BaseActivity;
import hwl.bysj.tomatolist.databinding.ActivityMainBinding;
import hwl.bysj.tomatolist.entity.TimeEntry;
import hwl.bysj.tomatolist.notebook.NoteBookActivity;
import hwl.bysj.tomatolist.studyroom.StudyRoomActivity;
import hwl.bysj.tomatolist.tomatoclock.ClockActivity;
import hwl.bysj.tomatolist.tomatoclock.TomatoClockActivity;
import hwl.bysj.tomatolist.treehole.TreeHoleActivity;
import hwl.bysj.tomatolist.user.login.LoginActivity;
import hwl.bysj.tomatolist.util.AndroidUtils;
import hwl.bysj.tomatolist.util.ICallback;
import hwl.bysj.tomatolist.util.SpUtils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //动态申请权限
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_NOTIFICATION_POLICY);
            permissionList.add(Manifest.permission.VIBRATE);
            permissionList.add(Manifest.permission.RECEIVE_BOOT_COMPLETED);
        }
        if(!permissionList.isEmpty()){
            String[] permissions =permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
        super.onCreate(savedInstanceState);

        // 找到 DrawerLayout 和 NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // 设置 OnNavigationItemSelectedListener 监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.study_room:
                        //点击自习室
                        Intent studyIntent = new Intent(MainActivity.this, StudyRoomActivity.class);
                        startActivity(studyIntent);
                        break;
                    case R.id.tree_hole:
                        //树洞
                        Intent treeIntent = new Intent(MainActivity.this, TreeHoleActivity.class);
                        startActivity(treeIntent);
                        break;
                    case R.id.note_book:
                        //记事本
                        Intent noteIntent = new Intent(MainActivity.this, NoteBookActivity.class);
                        startActivity(noteIntent);
                        break;
                    case R.id.login_out:
                        //登出
                        Intent outIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(outIntent);
                        break;
                    case R.id.tomato_clock:
                        //番茄钟
                        Intent tomatoIntent = new Intent(MainActivity.this, TomatoClockActivity.class);
                        startActivity(tomatoIntent);
                        break;
                    case R.id.about:
                        // 点击了 "关于我们" 菜单项
                        Intent aboutIntent = new Intent(MainActivity.this, InfoActivity.class);
                        startActivity(aboutIntent);
                        break;
                }

                // 选中后关闭侧滑菜单
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    @Override
    protected View initLayout() {
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        binding.imageMine.setOnClickListener(view->{
            drawerLayout.openDrawer(GravityCompat.START);
        });
        binding.tomatoClock.setOnClickListener(view->{
            Intent tomatoIntent = new Intent(MainActivity.this, TomatoClockActivity.class);
            startActivity(tomatoIntent);
        });
        binding.treeHole.setOnClickListener(view->{
            Intent treeIntent = new Intent(MainActivity.this, TreeHoleActivity.class);
            startActivity(treeIntent);
        });
        binding.studyRoom.setOnClickListener(view->{
            Intent studyIntent = new Intent(MainActivity.this, StudyRoomActivity.class);
            startActivity(studyIntent);
        });
        binding.noteBook.setOnClickListener(view->{
            Intent noteIntent = new Intent(MainActivity.this, NoteBookActivity.class);
            startActivity(noteIntent);
        });

    }
}
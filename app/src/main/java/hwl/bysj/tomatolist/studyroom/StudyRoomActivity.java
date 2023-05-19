package hwl.bysj.tomatolist.studyroom;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import hwl.bysj.tomatolist.R;
import hwl.bysj.tomatolist.base.BaseActivity;
import hwl.bysj.tomatolist.databinding.ActivityStudyRoomBinding;
import hwl.bysj.tomatolist.entity.BaseEntity;
import hwl.bysj.tomatolist.entity.Talk_Form;
import hwl.bysj.tomatolist.entity.UserConfigForm;
import hwl.bysj.tomatolist.http.RetrofitFactory;
import hwl.bysj.tomatolist.studyroom.write.WriteActivity;
import hwl.bysj.tomatolist.user.login.LoginViewModel;
import hwl.bysj.tomatolist.user.login.MyViewModel;
import hwl.bysj.tomatolist.util.ImageAdapter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudyRoomActivity extends BaseActivity {

    private int index = 0;
    private final int AUTOPLAY = 2;


    private ActivityStudyRoomBinding binding;
    List<Talk_Form> mFriendList=new ArrayList<>();
    private ForumAdapter forumAdapter;
    @Override
    protected View initLayout() {
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        binding = ActivityStudyRoomBinding.inflate(getLayoutInflater());
        initGallery();
        binding.myGallery.setOnItemClickListener((parent, view, position, id) -> Toast.makeText
                (this, "当前位置position:" + id + "的图片被选中了", Toast.LENGTH_SHORT).show());
    }

    //初始化画廊
    private void initGallery() {
        //用网络加载的方式未实现
        /*pictureAdapter = new PictureAdapter(this, mUrls);*/
       /* GlideAdapter glideAdapter = new GlideAdapter(this, mUrls);
        binding.myGallery.setAdapter(glideAdapter);*/
        //图片资源数组，本地图片资源
        int[] imageResIDs = new int[]{R.mipmap.w1, R.mipmap.w2, R.mipmap.w3};
        ImageAdapter adapter = new ImageAdapter(imageResIDs, this);
        binding.myGallery.setAdapter(adapter);
        /*binding.myGallery.setSpacing(20); //图片之间的间距*/
        binding.myGallery.setSelection((Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2) % imageResIDs.length);
        binding.myGallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initData() {
        //画廊自动播放
        autoPlay();
        SharedPreferences shp=this.getSharedPreferences("User_Tab", Context.MODE_PRIVATE);
        if (shp.getString("userimg",null)!=null){
            Glide.with(this).load(shp.getString("userimg",null)).into(binding.image1Head);
        }
        binding.forumName.setText(shp.getString(UserConfigForm.UN,""));
        binding.writeTextWord.setOnClickListener(v -> {
            if (shp.getInt("USER",0)==1){
                startActivity(new Intent(this, WriteActivity.class));
            }else {
                new AlertDialog.Builder(this).setMessage("未登录无法进操作").setPositiveButton("确认",(dialog, which)->{}).show();
            }
        });
        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            ImageView imageView = binding.image1Head;
            if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) { // 折叠状态
//                collapsingToolbar.setTitle("朋友圈");
                imageView.setVisibility(View.GONE);
            } else { // 非折叠状态
//                collapsingToolbar.setTitle("");
                imageView.setVisibility(View.VISIBLE);
            }
        });
        //设置item布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        //baseAdapter
        forumAdapter = new ForumAdapter(R.layout.item_forum_card, mFriendList);
        //设置不同数据发生改变
        forumAdapter.setDiffCallback(new ForumAdapter.DiffEventCallback());
        binding.recyclerView.setAdapter(forumAdapter);
        getEventListByPage();
    }


    //    private FriendAdapter adapter;
    private void getEventListByPage() {
        Observable<BaseEntity<List<Talk_Form>>> observable = RetrofitFactory.getInstence().API().upForum();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseEntity<List<Talk_Form>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseEntity<List<Talk_Form>> listBaseEntity) {
                        mFriendList.clear();
                        mFriendList = listBaseEntity.getData();
                        forumAdapter.setDiffNewData(mFriendList);
                        forumAdapter.notifyDataSetChanged();
                    }
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        Timer timer=new Timer();//实例化Timer类
        timer.schedule(new TimerTask(){
            public void run(){
                getEventListByPage();}},500);//五百毫秒
    }


    //在消息队列中实现对控件的更改，实现画廊的自动播放
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AUTOPLAY) {
                binding.myGallery.setSelection(index);
            }
        }
    };

    private void autoPlay(){
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = AUTOPLAY;
                index = binding.myGallery.getSelectedItemPosition();
                index++;
                handler.sendMessage(message);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task,3000,3000);
    }


}

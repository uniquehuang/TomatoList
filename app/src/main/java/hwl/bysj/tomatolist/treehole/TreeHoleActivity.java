package hwl.bysj.tomatolist.treehole;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import hwl.bysj.tomatolist.R;
import hwl.bysj.tomatolist.base.BaseActivity;
import hwl.bysj.tomatolist.databinding.ActivityTreeHoleBinding;
import hwl.bysj.tomatolist.entity.BaseEntity;
import hwl.bysj.tomatolist.entity.Comment;
import hwl.bysj.tomatolist.http.ProgressDialogSubscriber;
import hwl.bysj.tomatolist.http.RetrofitFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TreeHoleActivity extends BaseActivity {
    ActivityTreeHoleBinding binding;
    private CommentBaseAdapter adapter;
    private List<Comment> commentArrayList
            = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    adapter.setDiffNewData(commentArrayList);
                    adapter.setNewData(commentArrayList);
                    break;
            }
        }
    };
    @Override
    protected View initLayout() {
        return binding.getRoot();
    }

    @Override
    protected void initView() {
        binding = ActivityTreeHoleBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("VisibleForTests")
    @Override
    protected void initData() {

        binding.floating.setImageDrawable(getResources().getDrawable(R.mipmap.ic_letter));

        AnimationDrawable anim = new AnimationDrawable();
        anim.addFrame(getResources().getDrawable(R.drawable.frame1), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame2), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame3), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame4), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame5), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame6), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame7), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame8), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame9), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame10), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame11), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame12), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame13), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame14), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame15), 200);
        anim.addFrame(getResources().getDrawable(R.drawable.frame16), 200);
        anim.setOneShot(false);
        binding.atToolbarLayout.setBackground(anim);
        anim.start();

        /*Glide.with(this)
                .asDrawable()
                .load(R.drawable.tree)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        // 将 gif 图片解析成 Drawable 对象，然后设置为布局的背景
                        binding.atToolbarLayout.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {}
                });*/
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.content.recycleView.setLayoutManager(linearLayoutManager);
        //baseAdapter
        adapter = new CommentBaseAdapter(R.layout.item_comment, commentArrayList);
        //设置不同数据发生改变
        adapter.setDiffCallback(new CommentBaseAdapter.DiffEventCallback());
        binding.content.recycleView.setAdapter(adapter);
        //获取所有评论
        getCommentList();
    }

    private void getCommentList() {
        Observable<BaseEntity<List<Comment>>> observable = RetrofitFactory.getInstence().API().getComment();
        toSubscribe(observable,
                new ProgressDialogSubscriber<BaseEntity<List<Comment>>>(this) {
                    @Override
                    public void onNext(BaseEntity<List<Comment>> listBaseEntity) {
                        List<Comment> list = listBaseEntity.getData();
                        commentArrayList=list;
                        if (listBaseEntity.isSuccess() && list != null && list.size() > 0) {
                            commentArrayList = list;
                        }
                        adapter.setDiffNewData(commentArrayList);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                }
        );
    }

    public void onAddComment(View view) {
        EditText inputServer = new EditText(mContext);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("请输入留言")
                .setIcon(R.drawable.comment)
                .setView(inputServer)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = inputServer.getText().toString();
                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
                SharedPreferences shp=mContext.getSharedPreferences("User_Tab", Context.MODE_PRIVATE);

                    Observable<BaseEntity<String>> observable = RetrofitFactory.getInstence().API().postComment(
                            new Comment(text));
                    observable.subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new ProgressDialogSubscriber<BaseEntity<String>>(mContext) {
                                @Override
                                public void onNext(BaseEntity<String> noteEntityBaseEntity) {
                                    if (noteEntityBaseEntity.isSuccess()){
                                        Log.i("TAG", "onNext: "+noteEntityBaseEntity.getMsg());
                                        commentArrayList.clear();
                                        getCommentList();
                                        mHandler.sendEmptyMessage(0);
                                    }
                                }
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }
                            });

            }
        });
        builder.show();
    }
}
package hwl.bysj.tomatolist.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import hwl.bysj.tomatolist.base.BaseActivity;
import hwl.bysj.tomatolist.databinding.ActivityUserLoginBinding;
import hwl.bysj.tomatolist.MainActivity;
import hwl.bysj.tomatolist.user.register.RegisterActivity;


//同步异步问题，数据不统一
//context并不能finish一个activity


/**
 * @author dengfeng
 * @data 2023/4/12
 * @description
 */
public class LoginActivity extends BaseActivity {

    //视图绑定
    ActivityUserLoginBinding loginBinding;
    //ViewModel
    private LoginViewModel myLoginViewModel;

    MyViewModel myViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //获得viewModel实例
        myLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        super.onCreate(savedInstanceState);
        myViewModel = new ViewModelProvider(this, new SavedStateViewModelFactory(
                this.getApplication(),
                this,
                savedInstanceState)).get(MyViewModel.class);
    }

    @Nullable
    @Override
    protected View initLayout() {
        return loginBinding.getRoot();
    }

    @Override
    protected void initView() {
        loginBinding = ActivityUserLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        //注册
        loginBinding.userRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        /*Glide.with(this).load(R.drawable.login).into(loginBinding.imageView);*/


        //登录
        loginBinding.loginButton.setOnClickListener(view -> {
            login();
        });
        //观察者,并获取数据
        myLoginViewModel.loginResult.observe(this, user -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            //获取数据并存到myViewModel里面
            myViewModel.insertAll(myLoginViewModel.loginResult.getValue());
            myViewModel.update_IN();
            myViewModel.save();
            Log.v("!!!!!!!!!!!!!!!", String.valueOf(myViewModel.getIn().getValue()));
            finish();
        });
    }

    private void login() {
        String username = loginBinding.editTextTextPersonName.getText().toString();
        String password = loginBinding.editTextTextPassword.getText().toString();
        Context context = getApplicationContext();
        myLoginViewModel.login(context,username,password);
    }


    /*private void login(String account, String pwd) {
        if (TextUtils.isEmpty(loginBinding.editTextTextPersonName.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(loginBinding.editTextTextPassword.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        Observable<BaseEntity<User>> observable = RetrofitFactory.getInstence().API().login(new User(account,pwd));
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<User>>(this) {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseEntity<User> userBaseEntity) {
                        if (userBaseEntity.isSuccess()){
                            Log.i("TAG", "onNext: "+userBaseEntity.getErrorMsg());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else {
                            runOnUiThread(() -> Toast.makeText(LoginActivity.this,userBaseEntity.getErrorMsg(), Toast.LENGTH_SHORT).show());
                        }
                    }
                });

    }
*/
}

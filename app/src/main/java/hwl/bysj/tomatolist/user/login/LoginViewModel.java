package hwl.bysj.tomatolist.user.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.SavedStateHandle;
import hwl.bysj.tomatolist.R;
import hwl.bysj.tomatolist.base.BaseViewModel;
import hwl.bysj.tomatolist.entity.BaseEntity;
import hwl.bysj.tomatolist.entity.User;
import hwl.bysj.tomatolist.http.ProgressDialogSubscriber;
import hwl.bysj.tomatolist.http.RetrofitFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author dengfeng
 * @data 2023/4/12
 * @description
 */
public class LoginViewModel extends BaseViewModel {

    /*private final SavedStateHandle handle;*/
    //登录回调
    public MutableLiveData<User> loginResult = new MutableLiveData<>();
 /*   //用户账号
    private final String key_id = "id";
    //    用户名
    private final String key_name = getApplication().getResources().getString(R.string.USER_NAME);
    //    用户密码
    private final String key_password = getApplication().getResources().getString(R.string.PASSWORD);
    //    登录状态 1登录，0未登录
    private final String KEY_OUT_IN = getApplication().getResources().getString(R.string.OUT_IN);

    public LiveData<Integer> getId() {
        return handle.getLiveData(key_id);
    }//id

    public LiveData<String> getPassword() {
        return handle.getLiveData(key_password);
    }//密码

    public LiveData<String> getUsername() {
        return handle.getLiveData(key_name);
    }//用户名

    public LiveData<Integer> getIn() {
        return handle.getLiveData(KEY_OUT_IN);
    }//状态

    public void load() {
        SharedPreferences shp = getApplication().getSharedPreferences("User_Tab", Context.MODE_PRIVATE);

        String password = shp.getString(key_password, null);
        handle.set(key_password, password);

        String username = shp.getString(key_name, null);
        handle.set(key_name, username);

        int in_out = shp.getInt(KEY_OUT_IN, 0);
        handle.set(KEY_OUT_IN, in_out);


        int id = shp.getInt(key_id, 0);
        handle.set(key_id, id);

    }
    public void save() {
        SharedPreferences shp = getApplication().getSharedPreferences("User_Tab", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        //状态
        if (getIn().getValue() == null) {
            editor.putInt(KEY_OUT_IN, 0);
        } else {
            editor.putInt(KEY_OUT_IN, getIn().getValue());
        }

        editor.putInt(key_id, getId().getValue());
        editor.putString(key_password, getPassword().getValue());
        editor.putString(key_name, getUsername().getValue());
        editor.apply();
    }
    public void insertAll(User user){
        handle.set(key_id,user.getId());
        handle.set(key_name,user.getUsername());
        handle.set(key_password,user.getPassword());
    }


    public void insertId(int id) {
        handle.set(key_id, id);
    }

    public void insertPassword(String password) {
        handle.set(key_password, password);
    }

    public void insertName(String name) {
        handle.set(key_name, name);
    }


    public void update_IN() {
        handle.set(KEY_OUT_IN, 1);
    }

    public void update_OUT() {
        handle.set(KEY_OUT_IN, 0);
    }

    public void clearAll() {
        handle.set(key_password, null);
        handle.set(key_name, null);
        handle.set(key_id, 0);
        save();
    }

    public LoginViewModel(@NonNull @NotNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        boolean b = handle.contains(key_password)
                && handle.contains(key_name)
                && handle.contains(KEY_OUT_IN);
        if (!b) {
            load();
        }
    }*/





    /*setValue 只能在主线程调用，同步更新数据
    postValue 可在后台线程调用，其内部会切换到主线程调用 setValue*/
/*    public MutableLiveData<Integer> getLoginSuccess() {
        if (loginSuccess == null){
            loginSuccess = new MutableLiveData<>();
            loginSuccess.setValue(0);
        }
        return loginSuccess;
    }*/



    //登录
    //传入上下文，用户名，密码
   public void login (Context context, String userName, String password){

        if (TextUtils.isEmpty(userName)){
            Toast.makeText(context, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
       Observable<BaseEntity<User>> observable = RetrofitFactory.getInstence().API().login(userName,password);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressDialogSubscriber<BaseEntity<User>>(context) {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(BaseEntity<User> userBaseEntity) {
                        if (userBaseEntity.isSuccess()){
                            //输入数据
                            loginResult.setValue(userBaseEntity.getData());

                        }else {
                            Toast.makeText(context, userBaseEntity.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

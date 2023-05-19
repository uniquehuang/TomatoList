package hwl.bysj.tomatolist.user.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import hwl.bysj.tomatolist.R;
import hwl.bysj.tomatolist.entity.User;

public class MyViewModel extends AndroidViewModel {
    private final SavedStateHandle handle;
    //用户ID
    private final String key_id = "id";
    //    用户账号
    private final String key_phone = getApplication().getResources().getString(R.string.USER_PHONE);
    //    用户密码
    private final String key_password = getApplication().getResources().getString(R.string.PASSWORD);
    //    用户名
    private final String key_name = getApplication().getResources().getString(R.string.USER_NAME);

    //    登录状态 1登录，0未登录
    private final String KEY_OUT_IN = getApplication().getResources().getString(R.string.OUT_IN);
    //    点赞数
    private final String key_dz = getApplication().getResources().getString(R.string.USER_DZ);
    //    学习时长
    private final String key_study_time = getApplication().getResources().getString(R.string.USER_STUDY_TIME);

    //
    private final String key_userimg = getApplication().getResources().getString(R.string.USER_IMG);

    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        boolean b = handle.contains(key_phone)
                && handle.contains(key_password)
                && handle.contains(key_name)
                && handle.contains(KEY_OUT_IN)
                && handle.contains(key_dz)
                && handle.contains(key_study_time)
                && handle.contains(key_id)
                && handle.contains(key_userimg);
        if (!b) {
            load();
        }
    }


    public LiveData<Integer> getId() {
        return handle.getLiveData(key_id);
    }//id

    public LiveData<String> getPhone() {
        return handle.getLiveData(key_phone);
    }//账号

    public LiveData<String> getPassword() {
        return handle.getLiveData(key_password);
    }//密码

    public LiveData<String> getUsername() {
        return handle.getLiveData(key_name);
    }//用户名


    public LiveData<Integer> getIn() {
        return handle.getLiveData(KEY_OUT_IN);
    }//状态

    public LiveData<Integer> getDz() {
        return handle.getLiveData(key_dz);
    }//点赞

    public LiveData<Integer> getTimes() {
        return handle.getLiveData(key_study_time);
    }

    public LiveData<String> getUserImg() { return handle.getLiveData(key_userimg); }//头像

    public void load() {
        SharedPreferences shp = getApplication().getSharedPreferences("User_Tab", Context.MODE_PRIVATE);

        String img = shp.getString(key_userimg, null);
        handle.set(key_userimg, img);

        String phone = shp.getString(key_phone, null);
        handle.set(key_phone, phone);

        String password = shp.getString(key_password, null);
        handle.set(key_password, password);

        String username = shp.getString(key_name, null);
        handle.set(key_name, username);


        int in_out = shp.getInt(KEY_OUT_IN, 0);
        handle.set(KEY_OUT_IN, in_out);

        int dz = shp.getInt(key_dz, 0);
        handle.set(key_dz, dz);


        int time = shp.getInt(key_study_time, 0);
        handle.set(key_study_time, time);

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

        //时间
        if (getTimes().getValue() == null) {
            editor.putInt(key_study_time, 0);
        } else {
            editor.putInt(key_study_time, getTimes().getValue());
        }
        //点赞
        if (getDz().getValue() == null) {
            editor.putInt(key_dz, 0);
        } else {
            editor.putInt(key_dz, getDz().getValue());
        }

        //id
        editor.putString(key_userimg,getUserImg().getValue());
        editor.putInt(key_id, getId().getValue());
        editor.putString(key_phone, getPhone().getValue());
        editor.putString(key_password, getPassword().getValue());
        editor.putString(key_name, getUsername().getValue());
        editor.apply();
    }
    public void insertAll(User user_form){
        handle.set(key_userimg,user_form.getUserimg());
        handle.set(key_id,user_form.getId());
        handle.set(key_name,user_form.getUsername());
        handle.set(key_password,user_form.getPassword());
    }



    public void insertId(int id) {
        handle.set(key_id, id);
    }

    public void insertPhone(String phone) {
        handle.set(key_phone, phone);
    }

    public void insertPassword(String password) {
        handle.set(key_password, password);
    }

    public void insertName(String name) {
        handle.set(key_name, name);
    }


    public void insertTime(int time) {
        handle.set(key_study_time, time);
    }

    public void insertDz(int n) {
        handle.set(key_dz, n);
    }


    public void addTime(int n) {
        handle.set(key_study_time, getTimes().getValue() == null ? 1 : getTimes().getValue() + 1);
    }

    public void addDz() {
        handle.set(key_dz, getDz().getValue() == null ? 1 : getDz().getValue() + 1);
    }

    public void update_IN() {
        handle.set(KEY_OUT_IN, 1);
    }

    public void update_OUT() {
        handle.set(KEY_OUT_IN, 0);
    }

    public void clearAll() {
        handle.set(key_phone, null);
        handle.set(key_password, null);
        handle.set(key_name, null);
        handle.set(key_userimg, null);
        handle.set(key_dz, 0);
        handle.set(key_study_time, 0);
        handle.set(key_id, 0);
        save();
    }

}

package hwl.bysj.tomatolist.http;

/**
 * @author dengfeng
 * @data 2023/4/11
 * @description
 */


import android.media.Image;

import java.util.HashMap;
import java.util.List;

import hwl.bysj.tomatolist.entity.BaseEntity;
import hwl.bysj.tomatolist.entity.Comment;
import hwl.bysj.tomatolist.entity.Talk_Form;
import hwl.bysj.tomatolist.entity.User;
import hwl.bysj.tomatolist.http.config.URLConfig;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;


/**
 * @author yemao
 * @date 2017/4/9
 * @description API接口!
 */

public interface APIFunction {
    //登录接口
    /*@QueryMap注解会把参数拼接到url后面，所以它适用于GET请求；@Body会把参数放到请求体中，所以适用于POST请求。*/

    @POST(URLConfig.LOGIN)
    Observable<BaseEntity<User>> login(@Query("username") String username,@Query("password") String password);
    //注册接口
    @POST(URLConfig.REGISTER)
    Observable<BaseEntity<String>> register(@Body User register);


    @GET(URLConfig.BANNER)
    Observable<BaseEntity<List<Image>>> getImageList();

    //论坛上传
    @Multipart
    @POST(URLConfig.FORUM_TALK_DOWN)
    Observable<Object> downForum(@Query("username") String s, @Part("title") String title, @Part("article") String article, @Part List<MultipartBody.Part> file);
    //论坛获取
    @GET(URLConfig.FORUM_TALK_UP)
    Observable<BaseEntity<List<Talk_Form>>> upForum();

    //
    @POST("user/updateUserDZCountByUserName")
    Observable<Object> postDZ(@Query("dzcount") int dz,@Query("username") String root);

    //论坛点赞
    @POST("forum/updateForumByUserName")
    Observable<Object> DZ(@Query("dzflag") int dz,@Query("id") int id,@Query("username") String root);

    //获取评论接口
    @GET(URLConfig.GETCOMMENT)
    Observable<BaseEntity<List<Comment>>> getComment();

    @POST(URLConfig.POSTCOMMENT)
    Observable<BaseEntity<String>> postComment(@Body Comment comment);

}
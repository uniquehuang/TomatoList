package hwl.bysj.tomatolist.http.config;

/**
 * @author dengfeng
 * @data 2023/4/11
 * @description 网络接口地址
 */


public interface URLConfig {


    String LOGIN = "user/login"; //登录
    String REGISTER = "user/register";//注册


    String BANNER = "banner/json";  //轮播图片

    String FORUM_TALK_DOWN="forum/multiUpload";//上传论坛
    String FORUM_TALK_UP="forum/getForumItemList";//上传论坛

    String GETCOMMENT ="comment/getComment";//获取评论接口
    String POSTCOMMENT="comment/addComment";//评论上传接口
}
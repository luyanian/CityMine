package cn.soft_x.citymine.http;

/**
 * Created by Administrator on 2016-11-01.
 */
public class HttpUrl {
    //  2016-12-26 调货商
    /**
     * 李苗
     */
    //        public static final String API_HOST = "http://192.168.0.122:81";
    /**
     * 杨凌薇
     */
    //    public static final String API_HOST = "http://192.168.0.18:9191";
    /**
     * 辉哥
     */
    //        public static final String API_HOST = "http://192.168.0.58:84";
    /**
     * 毕萌萌
     */
//        public static final String API_HOST = "http://192.168.0.130:8791";
    /**
     * 外网
     */
//    public static final String API_HOST = "http://115.28.237.251:80";
    public static final String API_HOST = "http://123.207.150.153:84";
    /**
     *
     */
    public static final String API_PATH_1 = "/cmscm/api/";
    public static final String API_PATH_2 = "/cmscm/cgglApi/";
    public static final String API_PATH_3 = "/cmscm/cgglApi/";
    /**
     * 注册
     */
    public static final String REGISTER = API_HOST + API_PATH_1 + "register";
    /**
     * 上传图片
     */
    public static final String UploadFile = API_HOST+API_PATH_1+"tupiansc";
    /**
     * 登录
     */
    public static final String LOGIN = API_HOST + API_PATH_1 + "login";
    /**
     * 获取验证码
     */
    public static final String GET_YZM = API_HOST + API_PATH_1 + "getyzm";
    /**
     * 忘记密码
     */
    public static final String WJMM = API_HOST + API_PATH_1 + "wjmm";
    /**
     * 常见问题
     */
    public static final String CJWT = API_HOST + API_PATH_1 + "cjwt";
    /**
     * 更改手机号
     */
    public static final String GGSJ = API_HOST + API_PATH_1 + "updateDhhm";
    /**
     * 货车
     */
    public static final String HC = API_HOST + API_PATH_2 + "gethclb";
    /**
     * 销售人员
     */
    public static final String XSRY = API_HOST + API_PATH_2 + "getXsrylb";
    /**
     * 生成订单
     */
    public static final String SCDD = API_HOST + API_PATH_2 + "getscdd";
    /**
     * 消息
     */
    public static final String MSG_1 = API_HOST + API_PATH_2 + "xxinfo_yj";
    /**
     * 消息二级界面
     */
    public static final String MSG_2 = API_HOST + API_PATH_2 + "xxinfo_Ej";
    /**
     * 首页
     */
    public static final String HOME = API_HOST + API_PATH_1 + "showNews";
    /**
     * 附近的拆解企业
     */
    public static final String NEAR = API_HOST + API_PATH_2 + "getFjmap";
    /**
     * 修改密码
     */
    public static final String XGMM = API_HOST + API_PATH_1 + "xgmm";
    /**
     * 邀请函---调货商
     */
    public static final String YQH = API_HOST + API_PATH_1 + "showFbxx";
    /**
     * 邀请函--2--调货商
     */
    public static final String YQH_2 = API_HOST + API_PATH_1 + "getcgjh";
    /**
     * 发送邀请函
     */
    public static final String SEND_YQH = API_HOST + API_PATH_1 + "sendyqh";
}

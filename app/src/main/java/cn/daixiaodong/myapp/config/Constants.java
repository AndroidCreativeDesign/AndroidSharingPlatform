package cn.daixiaodong.myapp.config;

/**
 * 常量
 */
public class Constants {

    public static final String BMOB_APPLICATION_ID = "87c9c0ad96fabf08616bbbc095fe7ef5";

    public static final int TOPIC = 1;
    public static final String ASSOCIATION_TOPIC = "社团专题";

    // 表名
    public static final String TABLE_USER_JOIN = "user_join_all";
    public static final String TABLE_USER_COLLECT = "user_collect_all";
    public static final String TABLE_ASSOCIATION_PHOTOS = "association_photos";
    public static final String TABLE_ASSOCIATION = "association";

    // 不同类型
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_ASSOCIATION = 1;
    public static final int TYPE_RECRUITMENT = 2;
    public static final int TYPE_CROWDFUNDING = 3;


    // ACTION
    public static final String ACTION_USER_SIGN_IN = "cn.daixiaodong.myapp.USER_SIGN_IN";
    public static final String ACTION_USER_SIGN_OUT = "cn.daixiaodong.myapp.USER_SIGN_OUT";


    // 支付方式

    public static final int PAY_ALIPAY = 0;  // 支付宝
    public static final int PAY__WEPAY = 1;  // 微信
    public static final int OTHER = 2;      // 其他


    // 支付错误码
          /*
            错误码	原因
        -1	微信返回的错误码，可能是未安装微信，也可能是微信没获得网络权限等
        -2	微信支付用户中断操作
        -3	未安装微信支付插件
        102	设置了安全验证，但是签名或IP不对
        6001	支付宝支付用户中断操作
        4000	支付宝支付出错，可能是参数有问题
        1111	解析服务器返回的数据出错，可能是提交参数有问题
        2222	服务器端返回参数出错，可能是提交的参数有问题（如查询的订单号不存在）
        3333	解析服务器数据出错，可能是提交参数有问题
        5277	查询订单号时未输入订单号
        7777	微信客户端未安装
        8888	微信客户端版本不支持微信支付
        9010	网络异常，可能是没有给应用网络权限
        10003	商品名或详情不符合微信/支付宝的规定（如微信商品名不可以超过42个中文）*/
    public static final int ERROR_USER_CANCEL_ALIPAY = 6001;
    public static final int ERROR_USER_CANCEL_WEPAY = -2;
    public static final int ERROR_NO_INSTALL_PLUGIN = -3;
    public static final int ERROR_NETWORK_ERROR = 9010;
    public static final int ERROR_NO_INSTALL_WEIXIN = 7777;
    public static final int ERROR_WEIXIN_VERSION_NOT_SUPPORT_PAY = 8888;


}

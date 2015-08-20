package life.z_turn.app.config;

/**
 * 常量
 */
public class Constants {

    public static final String BMOB_APPLICATION_ID = "87c9c0ad96fabf08616bbbc095fe7ef5";

    public static final int TOPIC = 1;
    public static final String ASSOCIATION_TOPIC = "社团专题";
    public static final String STUDENT_UNION_TOPIC = "学生会专题";
    public static final int PAGE_SIZE = 10;

    // Key
    public static final String KEY_IS_RECEIVE_PUSH_MESSAGE = "isReceivePushMessage";


    // Extra Key
    public static final String EXTRA_KEY_ASSOCIATION_STRING_OBJECT = "association_string_object";
    public static final String EXTRA_KEY_DEPARTMENT_STRING_OBJECT = "department_string_object";
    public static final String EXTRA_KEY_COLLEGE = "college_name";
    public static final String EXTRA_KEY_DEPARTMENT_NAME = "department_name";
    public static final String EXTRA_KEY_IDEA_STRING_OBJECT = "idea_string_object";
    public static final String EXTRA_KEY_USER_STRING_OBJECT = "user_string_object";
    public static final String EXTRA_KEY_OBJECT_ID = "objectId";
    public static final String EXTRA_KEY_OBJECT_STRING = "objectStr";
    public static final String EXTRA_KEY_TYPE = "type";
    public static final String EXTRA_KEY_IMAGE_URL = "imgUrl";
    public static final String EXTRA_KEY_POSITION = "position";
    public static final String EXTRA_KEY_INTRODUCE_CONTENT = "introduce_content";
    // 表名
    public static final String TABLE_USER_JOIN = "user_join_all";
    public static final String TABLE_USER_COLLECT = "user_collect_all";
    public static final String TABLE_ASSOCIATION_PHOTO = "img";
    public static final String TABLE_ASSOCIATION = "association";
    public static final String TABLE_DISCOVER = "note";
    public static final String TABLE_IDEA = "idea";
    public static final String TABLE_PUSH_MESSAGE = "push_message";

    public static final String TABLE_STUDENT_UNION = "student_union";
    public static final String TABLE_STUDENT_UNION_DEPARTMENT = "student_union_department";
    public static final String TABLE_DEPARTMENT_PHOTO = "department_photo";
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String TABLE_TOPIC = "Topic";
    public static final String TABLE_COMMENT = "user_comment";

    // 事件
    public static final String EVENT_USER_CLICK_CREATE_IDEA_BUTTON = "user_create_idea";

    // 字段
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_UPDATED_AT = "updatedAt";
    public static final String COLUMN_USER = "user";
    public static final String COLUMN_TYPE_NAME = "typeName";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IDEA = "idea";
    public static final String COLUMN_FOLLOW = "follow";
    public static final String COLUMN_ASSOCIATION = "association";
    public static final String COLUMN_COLLECT_ID = "collectId";
    public static final String COLUMN_JOIN_ID = "joinId";
    public static final String COLUMN_IDEA_ID = "ideaId";

    public static final String COLUMN_ASSOCIATION_ID = "associationId";
    public static final String COLUMN_OBJECT_ID = "objectId";
    public static final String COLUMN_INTRODUCE = "introduce";
    public static final String COLUMN_DISCOVER_ID = "noteId";
    public static final String COLUMN_START_DATE = "startDate";
    public static final String COLUMN_END_DATE = "endDate";

    public static final String COLUMN_STUDENT_UNION_ID = "studentUnionId";
    public static final String COLUMN_DEPARTMENT_ID = "departmentId";
    public static final String COLUMN_DEPARTMENT = "department";
    public static final String COLUMN_STUDENT_UNION = "studentUnion";
    public static final String COLUMN_YEAR_ID = "yearId";
    public static final String COLUMN_ASSOCIATION_NAME = "name";
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_REPLY_TO_USER = "replyToUser";
    // 不同类型
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_ASSOCIATION = 1;
    public static final int TYPE_RECRUITMENT = 2;
    public static final int TYPE_CROWDFUNDING = 3;
    public static final int TYPE_STUDENT_UNION = 4;
    public static final int TYPE_STUDENT_UNION_DEPARTMENT = 5;
    public static final int TYPE_TOPIC = 6;


    // ACTION
    public static final String ACTION_USER_SIGN_IN = "life.z_turn.app.USER_SIGN_IN";
    public static final String ACTION_USER_SIGN_OUT = "life.z_turn.app.USER_SIGN_OUT";
    public static final String ACTION_USER_INFO_CHANGE = "life.z_turn.app.USER_INFO_CHANGE";

    // RequestCode
    public static final int REQUEST_MAGE_CAPTURE = 4560;
    public static final int REQUEST_CROP_IMAGE = 4561;
    public static final int REQUEST_LOGIN_UP_BY_PHONE = 1353;
    public static final int REQUEST_PICK = 4562;
    public static final int REQUEST_EDIT_USER_INFO = 4563;
    public static final int REQUEST_SIGN_UP_BY_EMAIL = 4564;
    public static final int REQUEST_SIGN_IN = 4565;

    public static final int REQUEST_EDIT_INTRODUCE = 3415;
    public static final int REQUEST_APPLY = 493;
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


    // API
    public static final String API_BASE = "http://www.z-turn.life/";
    public static final String API_USE_AGREEMENT = API_BASE + "use-agreement";

    // FLAG

    public static final int FLAG_NO_DATA = -1;


}

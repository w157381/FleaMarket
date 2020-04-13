package com.example.fleamarket.constants;

/**
 * 公用常量类。
 */
public class Constants {

    /**
     * 默认时间格式
     **/
    public static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    /**
     * Date默认时区
     **/
    public static final String DATE_TIMEZONE = "GMT+8";

    /**
     * UTF-8字符集
     **/
    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * GBK字符集
     **/
    public static final String CHARSET_GBK = "GBK";

    /**
     * JSON 应格式
     */
    public static final String FORMAT_JSON = "json";
    /**
     * XML 应格式
     */
    public static final String FORMAT_XML = "xml";

    /**
     * MD5签名方式
     */
    public static final String SIGN_METHOD_MD5 = "md5";
    /**
     * HMAC签名方式
     */
    public static final String SIGN_METHOD_HMAC = "hmac";


    public static final String SHOWAPI_APPID = "showapi_appid";
    public static final String SHOWAPI_TIMESTAMP = "showapi_timestamp";
    public static final String SHOWAPI_SIGN = "showapi_sign";
    public static final String SHOWAPI_SIGN_METHOD = "showapi_sign_method";

    public static final String SHOWAPI_RES_CODE = "showapi_res_code";
    public static final String SHOWAPI_RES_ERROR = "showapi_res_error";
    public static final String SHOWAPI_RES_BODY = "showapi_res_body";


    public static final String DEFAULT = "默认";


    //商品类别
    public static final String GOODS_type01 = "图书类";
    public static final String GOODS_type02 = "手机数码";
    public static final String GOODS_type03 = "生活百货";
    public static final String GOODS_type04 = "学习办公";
    public static final String GOODS_type05 = "代步工具";

    public static final String GOODS_type06 = "运动户外";
    public static final String GOODS_type07 = "鞋子";
    public static final String GOODS_type08 = "服饰";
    public static final String GOODS_type09 = "箱包";
    public static final String GOODS_type10 = "其他";

    //商品状态  -2:删除 -1:下架，0：售出，1：出售中  2:待审核 ,3 :已驳回
    public static final String GOODS_STATE_F2 = "已删除";
    public static final String GOODS_STATE_F1 = "已下架";
    public static final String GOODS_STATE_0 = "已售出";
    public static final String GOODS_STATE_1 = "出售中 ";
    public static final String GOODS_STATE_2 = "待审核";
    public static final String GOODS_STATE_3 = "已驳回";

    //交易状态   //  -1:交易失败  0：交易中 1：交易成功
    public static final String BIll_F1 = "交易失败";
    public static final String BIll_0 = "交易中";
    public static final String BIll_1 = "交易成功";

    //管理员操作语句
    public static final String ADMIN_ID = "888888";
    public static final String ADMIN_FENGHAO_TEXT = "违规处罚，封号操作";
    public static final String ADMIN_UNFENGHAO_TEXT = "违规处罚结束，取消封号，请以后遵守本系统规则";
    public static final String ADMIN_DONGJIE_TEXT = "违规处罚，冻结账号三天操作";
    public static final String ADMIN_UNDONGJIE_TEXT = "违规处罚结束，取消冻结账号，请以后遵守本系统规则";
    public static final String MY_PRE_NAME = "fleamarket";
    //管理员商品审核
    public static final String GOODS_AUDIT_PASS = "商品审核通过，商品已上架，记得及时回复买家信息哦~";
    public static final String GOODS_AUDIT_NOPASS = "商品被驳回，注意发布信息的规范性，请重新提交！";

}

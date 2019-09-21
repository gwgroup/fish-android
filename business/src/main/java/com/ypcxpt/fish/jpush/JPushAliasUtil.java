package com.ypcxpt.fish.jpush;

import android.text.TextUtils;

import com.ypcxpt.fish.App;

import static com.ypcxpt.fish.jpush.TagAliasOperatorHelper.ACTION_SET;
import static com.ypcxpt.fish.jpush.TagAliasOperatorHelper.sequence;

/**
 * @作者 Lenny
 * @时间 2019/1/5 16:06
 */
public class JPushAliasUtil {
    static String alias = null;
    static int action = -1;
    static boolean isAliasAction = false;

    public static void setAliasJPush(String aliasInfo) {
        alias = getInPutAlias(aliasInfo);
        if (TextUtils.isEmpty(alias)) {
            return;
        }
        isAliasAction = true;
        action = ACTION_SET;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        if (isAliasAction) {
            tagAliasBean.alias = alias;
        }
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(App.getInstance(), sequence, tagAliasBean);
    }

    /**
     * 获取输入的alias
     */
    public static String getInPutAlias(String aliasInfo) {
        String alias = aliasInfo;
        if (TextUtils.isEmpty(alias)) {
            return null;
        }
        if (!ExampleUtil.isValidTagAndAlias(alias)) {
            return null;
        }
        return alias;
    }
}

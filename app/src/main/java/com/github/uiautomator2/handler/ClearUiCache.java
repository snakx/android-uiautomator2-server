package com.github.uiautomator2.handler;

import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.utils.ReflectionUtils;

import org.json.JSONException;

/**
 * 清理界面缓存区数据
 * Created by Administrator on 2017/4/5.
 */

public class ClearUiCache extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        ReflectionUtils.clearAccessibilityCache();
        return getSuccessResult(true);
    }
}

package com.github.uiautomator2.handler;

import com.github.uiautomator2.common.exceptions.UiAutomator2Exception;
import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.utils.ReflectionUtils;
import com.github.uiautomator2.utils.XMLHierarchy;
import org.json.JSONException;
/**
 * 获取屏幕原始树结构
 * Created by Administrator on 2017/3/12.
 */

public class RawSource extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        try {
            ReflectionUtils.clearAccessibilityCache();
            String xmlString = XMLHierarchy.getRawStringHierarchy();
            return getSuccessResult(xmlString);
        }catch (UiAutomator2Exception e) {
            return getErrorResult(e.toString());
        }
    }
}

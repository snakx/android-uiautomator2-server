package com.github.uiautomator2.handler;

import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import static com.github.uiautomator2.utils.Device.getInstance;

/**
 * 打开快速设置
 * Created by Administrator on 2017/3/12.
 */

public class OpenQuickSetting extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (getInstance().getUiDevice().openQuickSettings()) {
            return getSuccessResult(true);
        } else {
            return getErrorResult("Device failed to open quickSetting.");
        }
    }
}

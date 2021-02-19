package com.github.uiautomator2.handler;

import android.support.test.uiautomator.UiObjectNotFoundException;
import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.executorserver.WDStatus;
import com.github.uiautomator2.model.AndroidElement;

import org.json.JSONException;

/**
 * 获取文本内容
 * Created by Administrator on 2017/3/12.
 */

public class GetText extends BaseCommandHandler {
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (command.isElementCommand()) {
            // Only makes sense on an element
            try {
                final AndroidElement el = command.getElement();
                return getSuccessResult(el.getText());
            } catch (final UiObjectNotFoundException e) {
                return new AndroidCommandResult(WDStatus.NO_SUCH_ELEMENT);
            } catch (final Exception e) { // handle NullPointerException
                return new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, e);
            }
        } else {
            return getErrorResult("Unable to get text without an element.");
        }
    }

}

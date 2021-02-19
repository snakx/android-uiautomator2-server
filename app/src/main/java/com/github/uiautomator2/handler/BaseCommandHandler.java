package com.github.uiautomator2.handler;

import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import com.github.uiautomator2.executorserver.WDStatus;
import com.github.uiautomator2.utils.Logger;
import org.json.JSONException;

/**
 * Base class for all handlers.
 *
 */
public abstract class BaseCommandHandler {

    protected final String ELEMENT_ID_KEY_NAME = "element";

    /**
     * Abstract method that handlers must implement.
     *
     * @param command A {@link AndroidCommand}
     * @return {@link AndroidCommandResult}
     * @throws JSONException
     */
    public abstract AndroidCommandResult execute(final AndroidCommand command)
            throws JSONException;

    /**
     * Returns a generic unknown error message along with your own message.
     *
     * @param msg
     * @return {@link AndroidCommandResult}
     */
    protected AndroidCommandResult getErrorResult(final String msg) {
        AndroidCommandResult AndroidResult = new AndroidCommandResult(WDStatus.UNKNOWN_ERROR, msg);
        Logger.error(AndroidResult.toString());
        return AndroidResult;
    }

    /**
     * Returns success along with the payload.
     *
     * @param value
     * @return {@link AndroidCommandResult}
     */
    protected AndroidCommandResult getSuccessResult(final Object value) {
        AndroidCommandResult AndroidResult=new AndroidCommandResult(WDStatus.SUCCESS, value);
        Logger.debug(AndroidResult.toString());
        return AndroidResult;
    }

}

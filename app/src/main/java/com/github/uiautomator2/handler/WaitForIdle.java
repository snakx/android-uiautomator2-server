package com.github.uiautomator2.handler;import com.github.uiautomator2.executorserver.AndroidCommand;import com.github.uiautomator2.executorserver.AndroidCommandResult;import org.json.JSONException;import org.json.JSONObject;import java.util.Hashtable;import com.github.uiautomator2.utils.Device;/** * This handler is used to wait idle for display in the Android UI. * */public class WaitForIdle extends BaseCommandHandler {    @Override    public AndroidCommandResult execute(final AndroidCommand command)            throws JSONException {        final Hashtable<String, Object> params = command.params();        long timeout = 10;        if (params.get("timeout") != JSONObject.NULL) {            timeout = (Integer) params.get("timeout");        }        Device.getInstance().getUiDevice().waitForIdle(timeout * 1000);        return getSuccessResult(true);    }}
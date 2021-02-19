package com.github.uiautomator2.handler;

import android.support.test.uiautomator.Configurator;
import com.github.uiautomator2.executorserver.AndroidCommand;
import com.github.uiautomator2.executorserver.AndroidCommandResult;
import org.json.JSONException;
import java.util.Hashtable;
import static com.github.uiautomator2.utils.API.API_18;

/**
 * 配置执行所数据的默认参数
 * Created by Administrator on 2017/6/27.
 */
public class ConfiguratorHandler extends BaseCommandHandler {
    private static final String ACTION_ACKNOWLEDGMENT_TIMEOUT = "actionAcknowledgmentTimeout";
    private static final String KEY_INJECTION_DELAY = "keyInjectionDelay";
    private static final String SCROLL_ACKNOWLEDGMENT_TIMEOUT = "scrollAcknowledgmentTimeout";
    private static final String WAIT_FOR_IDLE_TIMEOUT = "waitForIdleTimeout";
    private static final String WAIT_FOR_SELECTOR_TIMEOUT = "waitForSelectorTimeout";
    @Override
    public AndroidCommandResult execute(AndroidCommand command) throws JSONException {
        if (!API_18) {
            return getErrorResult("Device API version must >= 18!");
        }
        final Hashtable<String, Object> params = command.params();
        int value = -1; // negative value means default
        if (params.containsKey("value")) {
            value = (Integer) params.get("value");
        }
        String methodName = ((String) params.get("config"));
        //TODO: use reflection to invoke method would be more expandable; but Configurator is singleton
        Configurator configurator = Configurator.getInstance();
        switch (methodName) {
            case ACTION_ACKNOWLEDGMENT_TIMEOUT:
                if (value < 0) { // set to default when negative value
                    value = 3000;
                }
                configurator.setActionAcknowledgmentTimeout(value);
                break;
            case KEY_INJECTION_DELAY:
                if (value < 0) { // set to default when negative value
                    value = 0;
                }
                configurator.setKeyInjectionDelay(value);
                break;
            case SCROLL_ACKNOWLEDGMENT_TIMEOUT:
                if (value < 0) { // set to default when negative value
                    value = 200;
                }
                configurator.setScrollAcknowledgmentTimeout(value);
                break;
            case WAIT_FOR_IDLE_TIMEOUT:
                if (value < 0) { // set to default when negative value
                    value = 10000;
                }
                configurator.setWaitForIdleTimeout(value);
                break;
            case WAIT_FOR_SELECTOR_TIMEOUT:
                if (value < 0) { // set to default when negative value
                    value = 10000;
                }
                configurator.setWaitForSelectorTimeout(value);
                break;
            default:
                return getErrorResult("'configurator' command must contain 'config' key!");
        }
        return getSuccessResult(value);
    }
}

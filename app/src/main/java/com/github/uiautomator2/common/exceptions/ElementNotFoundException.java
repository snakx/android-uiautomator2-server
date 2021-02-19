package com.github.uiautomator2.common.exceptions;

import com.github.uiautomator2.model.Finder;

/**
 * An exception thrown when the element can not be found.
 */

@SuppressWarnings("serial")
public class ElementNotFoundException extends UiAutomator2Exception {
    final static String error = "Could not find an element using supplied strategy. ";

    public ElementNotFoundException() {
        super(error);
    }

    public ElementNotFoundException(final String extra) {
        super(error + extra);
    }

    public ElementNotFoundException(Finder finder) {
        super(failMessage(finder));
    }
    protected static String failMessage(Finder finder) {
        return "Could not find any element matching " + finder;
    }

    public ElementNotFoundException(Throwable t) {
        super(t);
    }
}

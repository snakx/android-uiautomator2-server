package com.github.uiautomator2.model;

import com.github.uiautomator2.model.internal.NativeAndroidBySelector;

/**
 * Mechanism used to locate elements within a document. In order to create your own locating
 * mechanisms, it is possible to subclass this class and override the protected methods as
 * required.
 */
public abstract class By {

    /**
     * @param id The value of the "id" attribute to search for
     *
     * @return a By which locates elements by the value of the "id" attribute.
     */
    public static By id(final String id) {
        if (id == null)
            throw new IllegalArgumentException("Cannot find elements with a null id attribute.");

        return new ById(id);
    }

    public static By name(String name) {
        if (name == null)
            throw new IllegalArgumentException("Cannot find elements with a null name attribute.");
        return new ByName(name);
    }

    public static By accessibilityId(final String text) {
        if (text == null)
            throw new IllegalArgumentException("Cannot find elements when text is null.");

        return new ByAccessibilityId(text);
    }

    public static By xpath(String xpathExpression) {
        if (xpathExpression == null)
            throw new IllegalArgumentException("Cannot find elements when xpath is null.");
        return new ByXPath(xpathExpression);
    }

    public static By className(String className) {
        if (className == null)
            throw new IllegalArgumentException("Cannot find elements when className is null.");
        return new ByClass(className);
    }

    public static By androidUiAutomator(String expression) {
        if (expression == null)
            throw new IllegalArgumentException("Cannot find elements when '-android uiautomator'" +
                    " is null.");
        return new ByAndroidUiAutomator(expression);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        By by = (By) o;

        return toString().equals(by.toString());
    }

    public abstract String getElementLocator();

    public abstract String getElementStrategy();

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        // A stub to prevent endless recursion in hashCode()
        return "[unknown locator]";
    }

    public static class ById extends By {
        private final String id;

        public ById(String id) {
            this.id = id;
        }

        @Override
        public String getElementLocator() {
            return id;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_NATIVE_ID;
        }

        @Override
        public String toString() {
            return "By.id: " + id;
        }
    }

    public static class ByClass extends By {
        private final String clazz;

        public ByClass(String clazz) {
            this.clazz = clazz;
        }

        @Override
        public String getElementLocator() {
            return clazz;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_CLASS;
        }

        @Override
        public String toString() {
            return "By.clazz: " + clazz;
        }
    }

    public static class ByAccessibilityId extends By {
        private final String accessibilityId;

        public ByAccessibilityId(String accessibilityId) {
            this.accessibilityId = accessibilityId;
        }

        @Override
        public String getElementLocator() {
            return accessibilityId;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_ACCESSIBILITY_ID;
        }

        @Override
        public String toString() {
            return "By.accessibilityId: " + accessibilityId;
        }
    }

    public static class ByXPath extends By {
        private final String xpathExpression;

        public ByXPath(String xpathExpression) {
            this.xpathExpression = xpathExpression;
        }

        @Override
        public String getElementLocator() {
            return xpathExpression;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_XPATH;
        }

        @Override
        public String toString() {
            return "By.xpath: " + xpathExpression;
        }
    }

    public static class ByAndroidUiAutomator extends By {
        private final String expresion;

        public ByAndroidUiAutomator(String expresion) {
            this.expresion = expresion;
        }

        @Override
        public String getElementLocator() {
            return expresion;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_ANDROID_UIAUTOMATOR;
        }

        @Override
        public String toString() {
            return "By.AndroidUiAutomator: " + expresion;
        }
    }

    public static class ByName extends By {
        private final String text;

        public ByName(String text) {
            this.text = text;
        }

        @Override
        public String getElementLocator() {
            return text;
        }

        @Override
        public String getElementStrategy() {
            return NativeAndroidBySelector.SELECTOR_NATIVE_NAME;
        }

        @Override
        public String toString() {
            return "By.name: " + text;
        }
    }
}

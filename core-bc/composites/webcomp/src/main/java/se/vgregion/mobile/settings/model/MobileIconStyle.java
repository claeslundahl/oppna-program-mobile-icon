package se.vgregion.mobile.settings.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 29/9-11
 * Time: 15:46
 */
public class MobileIconStyle {
    public static final String PREFIX = "MOBILE_ICON_STYLE_";

    private String key;
    private String value;

    public MobileIconStyle() {
    }

    public MobileIconStyle(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileIconStyle that = (MobileIconStyle) o;

        if (!key.equals(that.key)) return false;
        if (!value.equals(that.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}

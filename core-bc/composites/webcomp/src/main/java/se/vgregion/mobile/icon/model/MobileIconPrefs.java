package se.vgregion.mobile.icon.model;

import org.apache.commons.lang.StringUtils;

import javax.portlet.*;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 30/9-11
 * Time: 09:49
 */
public class MobileIconPrefs {

    private String title;
    private String imageId;
    private String iconStyle;
    private String targetUrl;
    private String target;
    private String widgetScript;
    private String counterService;
    private String updateInterval;

    public void fromRequest(ActionRequest request) {
        setTitle(request.getParameter("title"));
        setTargetUrl(request.getParameter("targetUrl"));
        setTarget(request.getParameter("target"));
        setImageId(request.getParameter("imageId"));
        setIconStyle(request.getParameter("iconStyle"));
        setWidgetScript(request.getParameter("widgetScript"));
        setCounterService(request.getParameter("counterService"));
        setUpdateInterval(request.getParameter("updateInterval"));
    }

    public void fromPreferences(PortletRequest request) {
        setTitle(fetchField("title", request));
        setTargetUrl(fetchField("targetUrl", request));
        setTarget(fetchField("target", request));
        setImageId(fetchField("imageId", request));
        setIconStyle(fetchField("iconStyle", request));
        setWidgetScript(fetchField("widgetScript", request));
        setCounterService(fetchField("counterService", request));
        setUpdateInterval(fetchField("updateInterval", request));
    }

    public void resetFields() {
        setTitle(null);
        setTargetUrl(null);
        setTarget(null);
        setImageId(null);
        setIconStyle(null);
        setWidgetScript(null);
        setCounterService(null);
        setUpdateInterval(null);
    }

    public void store(PortletPreferences preferences) throws ReadOnlyException, ValidatorException, IOException {
        preferences.setValue("title", getTitle());
        preferences.setValue("imageId", getImageId());
        preferences.setValue("iconStyle", getIconStyle());
        preferences.setValue("targetUrl", getTargetUrl());
        preferences.setValue("target", getTarget());
        preferences.setValue("widgetScript", getWidgetScript());
        preferences.setValue("counterService", getCounterService());
        preferences.setValue("updateInterval", getUpdateInterval());
        preferences.store();
    }

    public String getCounterService() {
        return counterService;
    }

    public String getCounterService(String defaultCounterService) {
        return isBlank(counterService) ? defaultCounterService : counterService;
    }

    public void setCounterService(String counterService) {
        this.counterService = counterService;
    }

    public String getIconStyle() {
        return iconStyle;
    }

    public String getIconStyle(String defaultIconStyle) {
        return isBlank(iconStyle) ? defaultIconStyle : iconStyle;
    }

    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getTarget() {
        return target;
    }

    public String getTarget(String defaultTarget) {
        return isBlank(target) ? defaultTarget : target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getTargetUrl(String defaultTargetUrl) {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle(String defaultTitle) {
        return isBlank(title) ? defaultTitle : title ;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateInterval() {
        return updateInterval;
    }

    public String getUpdateInterval(String defaultUpdateInterval) {
        return isBlank(updateInterval) ? defaultUpdateInterval : updateInterval;
    }

    public void setUpdateInterval(String updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getWidgetScript() {
        return widgetScript;
    }

    public String getWidgetScript(String defaultWidgetScript) {
        return isBlank(widgetScript) ? defaultWidgetScript : widgetScript;
    }

    public void setWidgetScript(String widgetScript) {
        this.widgetScript = widgetScript;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileIconPrefs that = (MobileIconPrefs) o;

        if (counterService != null ? !counterService.equals(that.counterService) : that.counterService != null)
            return false;
        if (iconStyle != null ? !iconStyle.equals(that.iconStyle) : that.iconStyle != null) return false;
        if (imageId != null ? !imageId.equals(that.imageId) : that.imageId != null) return false;
        if (target != null ? !target.equals(that.target) : that.target != null) return false;
        if (targetUrl != null ? !targetUrl.equals(that.targetUrl) : that.targetUrl != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (updateInterval != null ? !updateInterval.equals(that.updateInterval) : that.updateInterval != null)
            return false;
        if (widgetScript != null ? !widgetScript.equals(that.widgetScript) : that.widgetScript != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (imageId != null ? imageId.hashCode() : 0);
        result = 31 * result + (iconStyle != null ? iconStyle.hashCode() : 0);
        result = 31 * result + (targetUrl != null ? targetUrl.hashCode() : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (widgetScript != null ? widgetScript.hashCode() : 0);
        result = 31 * result + (counterService != null ? counterService.hashCode() : 0);
        result = 31 * result + (updateInterval != null ? updateInterval.hashCode() : 0);
        return result;
    }

    private String fetchField(String fieldName, PortletRequest request) {
        try {
            Field field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String value = (String) field.get(this);
            if (value != null) {
                return value;
            } else {
                PortletPreferences preferences = request.getPreferences();
                String preferencesValue = preferences.getValue(fieldName, "");
                return preferencesValue;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}

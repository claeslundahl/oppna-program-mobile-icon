package se.vgregion.mobileicon.controller;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import sun.reflect.Reflection;

import javax.portlet.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * User: pabe
 * Date: 2011-07-25
 * Time: 11:27
 */
@Controller
@RequestMapping(value = "EDIT")
@Scope(value = "session")
public class MobileIconEditController {

    private String title;
    private String imageId;
    private String targetUrl;
    private String target;
    private String widgetScript;
    private String counterService;

    @RenderMapping
    public String edit(RenderRequest request, Model model) {

        String title = fetchField("title", request);//
        String imageId = fetchField("imageId", request);
        String targetUrl = fetchField("targetUrl", request);
        String target = fetchField("target", request);
        String widgetScript = fetchField("widgetScript", request);
        String counterService = fetchField("counterService", request);

        model.addAttribute("title", title);
        model.addAttribute("imageId", imageId);
        model.addAttribute("targetUrl", targetUrl);
        model.addAttribute("target", target);
        model.addAttribute("widgetScript", widgetScript);
        model.addAttribute("counterService", counterService);

        Collection<String> mbDestinations = MessageBusUtil.getMessageBus().getDestinationNames();
        for (Iterator<String> it = mbDestinations.iterator(); it.hasNext(); ) {
            String destination = it.next();
            if (!destination.startsWith("vgr/counter")) {
                it.remove();
            }
        }
        model.addAttribute("allCounterServices", mbDestinations);

        return "edit";
    }

    private String fetchField(String fieldName, RenderRequest request) {
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

    @RenderMapping(params = {"action=editIconUrl"})
    public String editIconUrl(RenderRequest request, Model model) throws SystemException {

        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        long portletGroupId = themeDisplay.getScopeGroupId();

        List<IGFolder> folders = IGFolderLocalServiceUtil.getFolders(portletGroupId);

        for (IGFolder folder : folders) {
            if (folder.getName().equalsIgnoreCase("web content")) {
                List<IGImage> images = IGImageLocalServiceUtil.getImages(portletGroupId, folder.getFolderId());
                List<String> imageIds = new ArrayList<String>(images.size());
                for (IGImage image : images) {
                    imageIds.add(image.getSmallImageId() + "");
                }
                model.addAttribute("imageIds", imageIds);
                break;
            }
        }

        return "editIconUrl";
    }

    @ActionMapping(params = {"action=editIconUrl"})
    public void editIconUrl(ActionRequest request, ActionResponse response, Model model) {
        String title = request.getParameter("title");
        String targetUrl = request.getParameter("targetUrl");
        String target = request.getParameter("target");
        String widgetScript = request.getParameter("widgetScript");
        String counterService = request.getParameter("counterService");

        this.title = title;
        this.targetUrl = targetUrl;
        this.target = target;
        this.widgetScript = widgetScript;
        this.counterService = counterService;

        response.setRenderParameter("action", "editIconUrl");
    }


    @ActionMapping(params = {"action=submitIconUrl"})
    public void submitIconUrl(ActionRequest request, Model model) throws ReadOnlyException, ValidatorException,
            IOException {
        String imageId = request.getParameter("imageId");
        this.imageId = imageId;
    }

    @ActionMapping(params = {"action=save"})
    public void save(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException,
            IOException, PortletModeException, WindowStateException {
        String title = request.getParameter("title");
        String targetUrl = request.getParameter("targetUrl");
        String imageId = request.getParameter("imageId");
        String target = request.getParameter("target");
        String widgetScript = request.getParameter("widgetScript");
        String counterService = request.getParameter("counterService");

        PortletPreferences preferences = request.getPreferences();
        preferences.setValue("title", title);
        preferences.setValue("imageId", imageId);
        preferences.setValue("targetUrl", targetUrl);
        preferences.setValue("target", target);
        preferences.setValue("widgetScript", widgetScript);
        preferences.setValue("counterService", counterService);
        preferences.store();

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(LiferayWindowState.NORMAL);
    }

    @ActionMapping(params = {"action=cancel"})
    public void cancel(ActionResponse response) throws PortletModeException {
        this.title = null;
        this.imageId = null;
        this.targetUrl = null;
        this.target = null;
        this.widgetScript = null;
        this.counterService = null;

        response.setPortletMode(PortletMode.VIEW);
    }

}

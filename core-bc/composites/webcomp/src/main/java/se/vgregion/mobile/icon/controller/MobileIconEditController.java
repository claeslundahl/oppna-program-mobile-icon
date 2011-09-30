package se.vgregion.mobile.icon.controller;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.mobile.CompanyExpandoService;
import se.vgregion.mobile.icon.model.MobileIconPrefs;
import se.vgregion.mobile.settings.controller.MobileSettingsController;
import se.vgregion.mobile.settings.model.MobileIconStyle;

import javax.portlet.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * User: pabe
 * Date: 2011-07-25
 * Time: 11:27
 */
@Controller
@RequestMapping(value = "EDIT")
@Scope(value = "session")
public class MobileIconEditController implements Serializable {

    @Autowired
    private CompanyExpandoService companyExpandoService;

    private MobileIconPrefs prefs = new MobileIconPrefs();

    @RenderMapping
    public String edit(RenderRequest request, Model model) {
        prefs.fromPreferences(request);

        model.addAttribute("prefs", prefs);

        long companyId = lookupCompanyId(request);
        List<String> allIconStyles = new ArrayList<String>();
        List<ExpandoColumn> expandoColumns = companyExpandoService.getAllKeys(companyId);
        for (ExpandoColumn col: expandoColumns) {
            if (col.getName().startsWith(MobileIconStyle.PREFIX)) {
                allIconStyles.add(companyExpandoService.getSetting(col.getName(), companyId));
            }
        }
        model.addAttribute("allIconStyles", allIconStyles);

        Collection<String> result = new TreeSet();
        Collection<String> mbDestinations = MessageBusUtil.getMessageBus().getDestinationNames();
        for (String destination : mbDestinations) {
            if (destination.startsWith("vgr/counter")) {
                result.add(destination);
            }
        }
        model.addAttribute("allCounterServices", result);

        return "edit";
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
        prefs.fromRequest(request);

        response.setRenderParameter("action", "editIconUrl");
    }

    @ActionMapping(params = {"action=submitIconUrl"})
    public void submitIconUrl(ActionRequest request, Model model) throws ReadOnlyException, ValidatorException,
            IOException {
        String imageId = request.getParameter("imageId");
        prefs.setImageId(imageId);
    }

    @ActionMapping(params = {"action=save"})
    public void save(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException,
            IOException, PortletModeException, WindowStateException {
        prefs.fromRequest(request);
        prefs.store(request.getPreferences());
        prefs.resetFields();

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(LiferayWindowState.NORMAL);
    }

    @ActionMapping(params = {"action=cancel"})
    public void cancel(ActionResponse response) throws PortletModeException {
        prefs.resetFields();

        response.setPortletMode(PortletMode.VIEW);
    }

    private long lookupCompanyId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return themeDisplay.getCompanyId();
    }

}

package se.vgregion.mobile.icon.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.ValidatorException;
import javax.portlet.WindowStateException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import se.vgregion.liferay.expando.CompanyExpandoHelper;
import se.vgregion.mobile.icon.model.MobileIconPrefs;
import se.vgregion.mobile.settings.model.MobileIconStyle;

import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;

/**
 * User: pabe Date: 2011-07-25 Time: 11:27
 */
@Controller
@RequestMapping(value = "EDIT")
@Scope(value = "session")
public class MobileIconEditController implements Serializable {

    @Autowired
    private CompanyExpandoHelper companyExpandoHelper;

    private final MobileIconPrefs prefs = new MobileIconPrefs();

    @RenderMapping
    public String edit(RenderRequest request, Model model) {
        prefs.fromPreferences(request);

        model.addAttribute("prefs", prefs);

        long companyId = lookupCompanyId(request);
        List<String> allIconStyles = new ArrayList<String>();
        List<ExpandoColumn> expandoColumns = companyExpandoHelper.getAll(companyId);
        for (ExpandoColumn col : expandoColumns) {
            if (col.getName().startsWith(MobileIconStyle.PREFIX)) {
                allIconStyles.add(companyExpandoHelper.get(col.getName(), companyId));
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

    @ActionMapping(params = { "action=save" })
    public void save(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException,
            IOException, PortletModeException, WindowStateException {
        prefs.fromRequest(request);
        prefs.store(request.getPreferences());
        prefs.resetFields();

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(LiferayWindowState.NORMAL);
    }

    @ActionMapping(params = { "action=cancel" })
    public void cancel(ActionResponse response) throws PortletModeException, WindowStateException {
        prefs.resetFields();

        response.setPortletMode(PortletMode.VIEW);
        response.setWindowState(LiferayWindowState.NORMAL);
    }

    private long lookupCompanyId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return themeDisplay.getCompanyId();
    }

}

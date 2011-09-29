package se.vgregion.mobile.settings.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.mobile.CompanyExpandoService;
import se.vgregion.mobile.settings.model.MobileIconStyle;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 28/9-11
 * Time: 20:53
 */
@Controller
@RequestMapping("VIEW")
public class MobileSettingsController {

    @Autowired
    private CompanyExpandoService companyExpandoService;

    @RenderMapping
    public String defaultView(RenderRequest request, Model model) {
        long companyId = lookupCompanyId(request);

        List<MobileIconStyle> mobileIconStyles = new ArrayList<MobileIconStyle>();
        List<ExpandoColumn> companyExpandoColumns = companyExpandoService.getAllKeys(companyId);
        for (ExpandoColumn expandoColumn : companyExpandoColumns) {
            if (expandoColumn.getName().startsWith(MobileIconStyle.PREFIX)) {
                String key = expandoColumn.getName();
                String value = companyExpandoService.getSetting(key, companyId);

                mobileIconStyles.add(new MobileIconStyle(key, value));
            }
        }

        model.addAttribute("mobileIconStyles", mobileIconStyles);

        return "view";
    }

    @RenderMapping(params = "action=edit")
    public String editExpandoValue(RenderRequest request,
            @RequestParam(required = false, value = "expandoKey") String expandoKey,
            Model model) {
        MobileIconStyle mobileIconStyle = null;
        if (expandoKey == null) {
            mobileIconStyle = new MobileIconStyle(MobileIconStyle.PREFIX, "");
        } else {
            long companyId = lookupCompanyId(request);
            String value = companyExpandoService.getSetting(expandoKey, companyId);
            mobileIconStyle = new MobileIconStyle(expandoKey, value);
        }

        model.addAttribute("prefix", MobileIconStyle.PREFIX);
        model.addAttribute("currentMobileIconStyle", mobileIconStyle);

        return "edit";
    }

    @ActionMapping("saveMobileIconStyle")
    public void saveMobileIconStyle(ActionRequest request,
            @ModelAttribute MobileIconStyle mobileIconStyle,
            Model model) {
        try {
            long companyId = lookupCompanyId(request);
            companyExpandoService.setSetting(mobileIconStyle.getKey(), mobileIconStyle.getValue(), companyId);

            model.addAttribute("saveAction", mobileIconStyle.getKey());
        } catch (Exception ex) {
            model.addAttribute("saveActionFailed", mobileIconStyle.getKey());
        }

    }

    @ActionMapping("delete")
    public void deletaMobileIconStyle(ActionRequest request, @RequestParam("expandoKey") String expandoKey,
            Model model) {
        try {
            long companyId = lookupCompanyId(request);
            companyExpandoService.delete(companyId, expandoKey);

            model.addAttribute("removeAction", expandoKey);
        } catch (Exception ex) {
            model.addAttribute("removeActionFailed", expandoKey);
        }
    }

    private long lookupCompanyId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return themeDisplay.getCompanyId();
    }
}

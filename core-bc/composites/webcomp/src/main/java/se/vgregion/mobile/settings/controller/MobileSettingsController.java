package se.vgregion.mobile.settings.controller;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.mobile.CommunityExpandoService;
import se.vgregion.mobile.CompanyExpandoService;
import se.vgregion.mobile.settings.model.MobileIconStyle;
import se.vgregion.mobile.settings.model.MobileStartPage;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileSettingsController.class);

    @Autowired
    private CompanyExpandoService companyExpandoService;

    @Autowired
    private CommunityExpandoService communityExpandoService;

    @Autowired
    private LayoutLocalService layoutLocalService;

    @Autowired
    private GroupLocalService groupLocalService;

    @Value("${community.name}")
    private String communityName;

    @RenderMapping
    public String defaultView(RenderRequest request, Model model) {
        long companyId = lookupCompanyId(request);
        String languageId = lookupLanguageId(request);

        List<MobileIconStyle> mobileIconStyles = lookupMobileIconStyles(companyId);
        model.addAttribute("mobileIconStyles", mobileIconStyles);

        List<MobileStartPage> topPages = lookupMobileStartPages(companyId, languageId);
        model.addAttribute("topPages", topPages);

        Long layoutId = communityExpandoService.getSetting(MobileStartPage.MOBILE_START_PAGE_KEY, companyId);
        if (layoutId == null) {
            model.addAttribute("startPage", new MobileStartPage());
        } else {
            try {
                Layout layout = layoutLocalService.getLayout(layoutId);
                MobileStartPage startPage = new MobileStartPage();
                startPage.setLayoutId(layoutId);
                startPage.setPageTitle(layout.getName(languageId, true));
                startPage.setFriendlyUrl(layout.getFriendlyURL());
                startPage.setHidden(layout.isHidden());

                model.addAttribute("startPage", startPage);
            } catch (Exception e) {
                LOGGER.error("Configured mobile start page cannot be found", e);
                model.addAttribute("startPage", new MobileStartPage());
            }
        }

        return "view";
    }

    private List<MobileStartPage> lookupMobileStartPages(long companyId, String languageId) {
        List<MobileStartPage> topPages = new ArrayList<MobileStartPage>();

        Group community = null;
        try {
            community = groupLocalService.getGroup(companyId, communityName);
        } catch (Exception e) {
            LOGGER.error("Could not find community [" + communityName + "] - Mobile startpage cannot be configured");
        }

        if (community != null) {
            List<Layout> layouts = null;
            try {
                layouts = layoutLocalService.getLayouts(community.getGroupId(), true, 0L);
            } catch (SystemException e) {
                LOGGER.error("Failed to find layouts in community [" + communityName + "] - Mobile startpage cannot be " +
                        "configured");
            }
            if (layouts != null) {
                for (Layout layout : layouts) {
                    MobileStartPage topPage = new MobileStartPage();
                    topPage.setLayoutId(layout.getLayoutId());
                    topPage.setPageTitle(layout.getName(languageId, true));
                    topPage.setFriendlyUrl(layout.getFriendlyURL());
                    topPage.setHidden(layout.isHidden());

                    topPages.add(topPage);
                }
            }
        }

        return topPages;
    }

    private List<MobileIconStyle> lookupMobileIconStyles(long companyId) {
        List<MobileIconStyle> mobileIconStyles = new ArrayList<MobileIconStyle>();
        List<ExpandoColumn> companyExpandoColumns = companyExpandoService.getAllKeys(companyId);
        for (ExpandoColumn expandoColumn : companyExpandoColumns) {
            if (expandoColumn.getName().startsWith(MobileIconStyle.PREFIX)) {
                String key = expandoColumn.getName();
                String value = companyExpandoService.getSetting(key, companyId);

                mobileIconStyles.add(new MobileIconStyle(key, value));
            }
        }
        return mobileIconStyles;
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
    public void deleteMobileIconStyle(ActionRequest request, @RequestParam("expandoKey") String expandoKey,
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
    private String lookupLanguageId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        return themeDisplay.getLanguageId();
    }
}

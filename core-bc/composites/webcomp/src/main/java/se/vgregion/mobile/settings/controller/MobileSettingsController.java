package se.vgregion.mobile.settings.controller;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.GroupLocalService;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.JournalArticleLocalService;
import com.liferay.portlet.journal.service.JournalStructureLocalService;
import com.liferay.portlet.journal.service.JournalTemplateLocalService;
import org.apache.commons.lang.StringUtils;
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
import se.vgregion.mobile.settings.model.MobileArticle;
import se.vgregion.mobile.settings.model.MobileIconStyle;
import se.vgregion.mobile.settings.model.MobilePage;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    @Autowired
    private JournalArticleLocalService journalArticleLocalService;

    @Autowired
    private JournalStructureLocalService journalStructureLocalService;

    @Autowired
    private JournalTemplateLocalService journalTemplateLocalService;

    @Value("${mobile.community.name}")
    private String communityName;

    @Value("${public.mobile.community.name}")
    private String publicCommunityName;

    @RenderMapping
    public String defaultView(RenderRequest request, Model model) {
        long companyId = lookupCompanyId(request);
        String languageId = lookupLanguageId(request);
        Long groupId = lookupGroupId(request);
        Long publicGroupId = lookupPublicGroupId(request);

        List<MobileIconStyle> mobileIconStyles = lookupMobileIconStyles(companyId);
        model.addAttribute("mobileIconStyles", mobileIconStyles);

        Map<Long, String> topPages = lookupTopPages(languageId, groupId);
        model.addAttribute("topPages", topPages);

        Map<Long, String> publicPages = lookupPublicPages(languageId, publicGroupId);
        model.addAttribute("publicPages", publicPages);

        MobilePage startPage = lookupMobileStartPage(companyId, languageId, groupId);
        model.addAttribute("startPage", startPage);

        MobilePage loginPage = lookupMobileLoginPage(companyId, languageId, groupId);
        model.addAttribute("loginPage", loginPage);

        MobileArticle workArticle = lookupMobileArticle(companyId, languageId, groupId, MobileArticle.PREFIX + "1");
        model.addAttribute("workArticle", workArticle);
        MobileArticle searchArticle = lookupMobileArticle(companyId, languageId, groupId, MobileArticle.PREFIX + "2");
        model.addAttribute("searchArticle", searchArticle);
        MobileArticle userArticle = lookupMobileArticle(companyId, languageId, groupId, MobileArticle.PREFIX + "3");
        model.addAttribute("userArticle", userArticle);

        return "view";
    }

    private MobileArticle lookupMobileArticle(long companyId, String languageId, Long groupId, String expandoKey) {
        MobileArticle article = new MobileArticle(expandoKey);
        try {
            String articleId = communityExpandoService.getStringSetting(expandoKey, companyId);

            if (articleId != null) {
                JournalArticle journalArticle = journalArticleLocalService.getLatestArticle(groupId, articleId);
                article.setArticleId(articleId);
                article.setVersion(journalArticle.getVersion());
                article.setArticleName(journalArticle.getTitle());

                String structureId = journalArticle.getStructureId();
                if (StringUtils.isNotBlank(structureId)) {
                    JournalStructure journalStructure = journalStructureLocalService.getStructure(groupId,
                            structureId);
                    article.setStructureName(journalStructure.getName());
                }

                String tempaleId = journalArticle.getTemplateId();
                if (StringUtils.isNotBlank(structureId)) {
                    JournalTemplate journalTemplate = journalTemplateLocalService.getTemplate(groupId,
                            tempaleId);
                    article.setTemplateName(journalTemplate.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Configured article cannot be found", e);
        }

        return article;
    }

    private MobilePage lookupMobileStartPage(long companyId, String languageId, Long groupId) {
        MobilePage startPage = new MobilePage(MobilePage.MOBILE_START_PAGE_KEY);
        try {
            Long layoutId = communityExpandoService.getLongSetting(MobilePage.MOBILE_START_PAGE_KEY, companyId);
            if (layoutId != null) {
                Layout layout = layoutLocalService.getLayout(groupId, true, layoutId);
                startPage.setLayoutId(layoutId);
                startPage.setPageTitle(layout.getName(languageId, true));
                startPage.setFriendlyUrl(layout.getFriendlyURL());
                startPage.setHidden(layout.isHidden());
            }
        } catch (Exception e) {
            LOGGER.error("Configured mobile start page cannot be found", e);
        }
        return startPage;
    }

    private MobilePage lookupMobileLoginPage(long companyId, String languageId, Long groupId) {
        MobilePage loginPage = new MobilePage(MobilePage.MOBILE_LOGIN_PAGE_KEY);
        try {
            Long layoutId = communityExpandoService.getLongSetting(MobilePage.MOBILE_LOGIN_PAGE_KEY, companyId);
            if (layoutId != null) {
                Layout layout = layoutLocalService.getLayout(groupId, true, layoutId);
                loginPage.setLayoutId(layoutId);
                loginPage.setPageTitle(layout.getName(languageId, true));
                loginPage.setFriendlyUrl(layout.getFriendlyURL());
                loginPage.setHidden(layout.isHidden());
            }
        } catch (Exception e) {
            LOGGER.error("Configured mobile start page cannot be found", e);
        }
        return loginPage;
    }

    private Map<Long, String> lookupTopPages(String languageId, Long groupId) {
        Map<Long, String> topPages = new TreeMap<Long, String>();
        if (groupId != null) {
            List<Layout> layouts = null;
            try {
                layouts = layoutLocalService.getLayouts(groupId, true, 0L);
            } catch (SystemException e) {
                LOGGER.error("Failed to find layouts in community [" + communityName + "] - Mobile startpage cannot be " +
                        "configured");
            }
            if (layouts != null) {
                for (Layout layout : layouts) {
                    String label = layout.getName(languageId, true) + (layout.isHidden() ? " [H]" : "");

                    topPages.put(layout.getLayoutId(), label);
                }
            }
        }

        return topPages;
    }

    private Map<Long, String> lookupPublicPages(String languageId, Long groupId) {
        Map<Long, String> topPages = new TreeMap<Long, String>();
        if (groupId != null) {
            List<Layout> layouts = null;
            try {
                layouts = layoutLocalService.getLayouts(groupId, false, 0L);
            } catch (SystemException e) {
                LOGGER.error("Failed to find layouts in community [" + communityName + "] - Mobile loginpage cannot " +
                        "be " +
                        "configured");
            }
            if (layouts != null) {
                for (Layout layout : layouts) {
                    String label = layout.getName(languageId, true) + (layout.isHidden() ? " [H]" : "");

                    topPages.put(layout.getLayoutId(), label);
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
            LOGGER.error("saveMobileIconStyle", ex);
            model.addAttribute("saveActionFailed", mobileIconStyle.getKey());
        }
    }

    @ActionMapping("saveMobilePage")
    public void saveMobileStartPage(ActionRequest request,
            @ModelAttribute MobilePage mobilePage, Model model) {
        try {
            long companyId = lookupCompanyId(request);
            communityExpandoService.setSetting(mobilePage.getExpandoKey()
                    , mobilePage.getLayoutId()
                    , companyId);

            model.addAttribute("saveActionPage", mobilePage.getExpandoKey());
        } catch (Exception ex) {
            LOGGER.error("saveMobilePage", ex);
            model.addAttribute("saveActionPageFailed", mobilePage.getExpandoKey());
        }
    }

    @ActionMapping("saveMobileArticle")
    public void saveMobileArticle(ActionRequest request,
            @ModelAttribute MobileArticle mobileArticle, Model model) {
        try {
            long companyId = lookupCompanyId(request);
            communityExpandoService.setSetting(mobileArticle.getExpandoKey()
                    , mobileArticle.getArticleId()
                    , companyId);

            model.addAttribute("saveActionArticle", mobileArticle.getExpandoKey());
        } catch (Exception ex) {
            LOGGER.error("saveMobileArticle", ex);
            model.addAttribute("saveActionArticleFailed", mobileArticle.getExpandoKey());
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

    private Long lookupGroupId(PortletRequest request) {
        Group community = null;
        try {
            community = groupLocalService.getGroup(lookupCompanyId(request), communityName);
            return community.getGroupId();
        } catch (Exception e) {
            LOGGER.error("Could not find community [" + communityName + "] - Mobile startpage cannot be configured");
        }
        return null;
    }

    private Long lookupPublicGroupId(PortletRequest request) {
        Group community = null;
        try {
            community = groupLocalService.getGroup(lookupCompanyId(request), publicCommunityName);
            return community.getGroupId();
        } catch (Exception e) {
            LOGGER.error("Could not find community [" + publicCommunityName + "] - Mobile loginpage cannot be " +
                    "configured");
        }
        return null;
    }

}

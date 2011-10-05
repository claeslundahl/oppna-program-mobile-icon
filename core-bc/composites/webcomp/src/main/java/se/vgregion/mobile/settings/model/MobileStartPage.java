package se.vgregion.mobile.settings.model;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 15:53
 */
public class MobileStartPage {
    public static final String MOBILE_START_PAGE_KEY = "MOBILE_START_PAGE";

    private String expandoKey = MOBILE_START_PAGE_KEY;
    private Long layoutId;
    private String friendlyUrl;
    private String pageTitle;
    private boolean hidden;

    public MobileStartPage() {
    }

    public MobileStartPage(Long layoutId, String pageTitle, String friendlyUrl, boolean hidden) {
        this.friendlyUrl = friendlyUrl;
        this.layoutId = layoutId;
        this.pageTitle = pageTitle;
        this.hidden = hidden;
    }

    public String getExpandoKey() {
        return expandoKey;
    }

    public String getFriendlyUrl() {
        return friendlyUrl;
    }

    public void setFriendlyUrl(String friendlyUrl) {
        this.friendlyUrl = friendlyUrl;
    }

    public Long getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(Long layoutId) {
        this.layoutId = layoutId;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileStartPage that = (MobileStartPage) o;

        if (layoutId != null ? !layoutId.equals(that.layoutId) : that.layoutId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return layoutId != null ? layoutId.hashCode() : 0;
    }
}

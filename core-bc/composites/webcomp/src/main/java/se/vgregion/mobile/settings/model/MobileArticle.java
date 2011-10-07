package se.vgregion.mobile.settings.model;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 6/10-11
 * Time: 11:08
 */
public class MobileArticle {
    public static final String PREFIX = "MOBILE_ARTICLE_";

    private String expandoKey;
    private String articleId;
    private Double version;
    private String articleName;
    private String structureName;
    private String templateName;

    public MobileArticle() {
    }

    public MobileArticle(String expandoKey) {
        this.expandoKey = expandoKey;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getExpandoKey() {
        return expandoKey;
    }

    public void setExpandoKey(String expandoKey) {
        this.expandoKey = expandoKey;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileArticle that = (MobileArticle) o;

        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) return false;
        if (articleName != null ? !articleName.equals(that.articleName) : that.articleName != null) return false;
        if (expandoKey != null ? !expandoKey.equals(that.expandoKey) : that.expandoKey != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = expandoKey != null ? expandoKey.hashCode() : 0;
        result = 31 * result + (articleId != null ? articleId.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (articleName != null ? articleName.hashCode() : 0);
        return result;
    }
}

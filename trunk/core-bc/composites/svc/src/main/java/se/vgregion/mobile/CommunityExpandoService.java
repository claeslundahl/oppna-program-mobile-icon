package se.vgregion.mobile;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:47
 */
public interface CommunityExpandoService {
    void setSetting(String columnName, Long value, long companyId, long communityId);

    void setSetting(String columnName, String value, long companyId, long communityId);

    Long getLongSetting(String columnName, long companyId, long communityId);

    String getStringSetting(String columnName, long companyId, long communityId);
}

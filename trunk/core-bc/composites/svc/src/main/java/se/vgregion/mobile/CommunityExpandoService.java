package se.vgregion.mobile;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:47
 */
public interface CommunityExpandoService {
    void setSetting(String columnName, Long value, long companyId);

    void setSetting(String columnName, String value, long companyId);

    Long getLongSetting(String columnName, long companyId);

    String getStringSetting(String columnName, long companyId);
}

package se.vgregion.mobile;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:47
 */
public interface CommunityExpandoService {
    void setSetting(String columnName, Long value, long companyId);

    Long getSetting(String columnName, long companyId);
}

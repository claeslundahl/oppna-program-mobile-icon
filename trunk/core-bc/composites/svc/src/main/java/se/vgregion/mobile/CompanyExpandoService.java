package se.vgregion.mobile;

import com.liferay.portlet.expando.model.ExpandoColumn;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 29/9-11
 * Time: 15:55
 */
public interface CompanyExpandoService {
    void setSetting(String columnName, String value, long companyId);

    String getSetting(String columnName, long companyId);

    List<ExpandoColumn> getAllKeys(long companyId);

    void delete(long companyId, String columnName);
}

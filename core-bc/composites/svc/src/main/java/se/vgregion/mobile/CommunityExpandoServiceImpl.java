package se.vgregion.mobile;

import com.liferay.portal.model.Company;
import com.liferay.portal.model.Group;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:47
 */
public class CommunityExpandoServiceImpl extends BaseExpandoServiceImpl implements CommunityExpandoService {

    @Override
    public void setSetting(String columnName, Long value, long companyId) {
        setSetting(COMMUNITY_CLASSNAME, columnName, value, companyId, ExpandoColumnConstants.LONG);
    }

    @Override
    public void setSetting(String columnName, String value, long companyId) {
        setSetting(COMMUNITY_CLASSNAME, columnName, value, companyId, ExpandoColumnConstants.STRING);
    }

    @Override
    public Long getLongSetting(String columnName, long companyId) {
        try {
            return (Long)getSetting(COMMUNITY_CLASSNAME, columnName, companyId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getStringSetting(String columnName, long companyId) {
        try {
            return (String)getSetting(COMMUNITY_CLASSNAME, columnName, companyId);
        } catch (Exception e) {
            return "";
        }
    }

    private final static String COMMUNITY_CLASSNAME = Group.class.getName();

}

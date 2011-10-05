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
    public void setSetting(String columnName, String value, long companyId) {
        setSetting(COMMUNITY_CLASSNAME, columnName, value, companyId, EXPANDO_TYPE);
    }

    @Override
    public Long getSetting(String columnName, long companyId) {
        try {
            return new Long(getSetting(COMMUNITY_CLASSNAME, columnName, companyId));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private final static String COMMUNITY_CLASSNAME = Group.class.getName();
    private static final int EXPANDO_TYPE = ExpandoColumnConstants.LONG;

}

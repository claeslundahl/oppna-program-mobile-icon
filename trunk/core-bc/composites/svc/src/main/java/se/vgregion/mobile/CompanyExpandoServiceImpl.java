package se.vgregion.mobile;

import com.liferay.portlet.expando.model.ExpandoColumn;

import com.liferay.portal.model.Company;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;

import java.util.List;

public class CompanyExpandoServiceImpl extends BaseExpandoServiceImpl implements CompanyExpandoService {

	@Override
    public void setSetting(String columnName, String value, long companyId) {
        setSetting(COMPANY_CLASSNAME, columnName, value, companyId, EXPANDO_TYPE);
	}

    @Override
    public String getSetting(String columnName, long companyId) {
        return getSetting(COMPANY_CLASSNAME, columnName, companyId);
    }

    @Override
    public List<ExpandoColumn> getAllKeys(long companyId) {
        return getAllKeys(COMPANY_CLASSNAME, companyId);
    }

    @Override
    public void delete(long companyId, String columnName) {
        delete(COMPANY_CLASSNAME, columnName, companyId);
    }

	private static final String COMPANY_CLASSNAME = Company.class.getName();
    private static final int EXPANDO_TYPE = ExpandoColumnConstants.STRING;
}

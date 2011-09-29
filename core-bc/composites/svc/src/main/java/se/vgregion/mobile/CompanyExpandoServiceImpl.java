package se.vgregion.mobile;

import com.liferay.portlet.expando.model.ExpandoColumn;
import org.springframework.beans.factory.annotation.Autowired;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Company;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;

import java.util.List;

public class CompanyExpandoServiceImpl implements CompanyExpandoService {

	@Autowired
	private ExpandoColumnLocalService expandoColumnService;

	@Autowired
	private ExpandoTableLocalService expandoTableService;

	@Autowired
	private ExpandoValueLocalService expandoValueService;

	@Override
    public void setSetting(String columnName, String value, long companyId) {
		try {
			expandoValueService.addValue(companyId, COMPANY_CLASSNAME, ExpandoTableConstants.DEFAULT_TABLE_NAME,
			        columnName, companyId, value);
		} catch (PortalException e) {
			// If table don't exists we try to create it.
			if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
				createExpandoTabel(columnName, value, companyId);
			} else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
				// If column don't exists we try to create it.
				createExpandoColumn(columnName, value, companyId);
			} else {
				throw new RuntimeException(e);
			}
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
    public String getSetting(String columnName, long companyId) {

		String value = "";
		try {
			value = expandoValueService.getData(companyId, COMPANY_CLASSNAME,
			        ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId, "");

		} catch (PortalException e) {
			throw new RuntimeException(e);
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
		return value;
	}

    public List<ExpandoColumn> getAllKeys(long companyId) {
        try {
            return expandoColumnService.getDefaultTableColumns(companyId, COMPANY_CLASSNAME);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long companyId, String columnName) {
        try {
            expandoColumnService.deleteColumn(companyId, COMPANY_CLASSNAME, ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createExpandoColumn(String columnName, String value, long companyId) {
		try {
			long tableId = expandoTableService.getDefaultTable(companyId, COMPANY_CLASSNAME).getTableId();
			expandoColumnService.addColumn(tableId, columnName, ExpandoColumnConstants.STRING);
			setSetting(columnName, value, companyId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void createExpandoTabel(String columnName, String value, long companyId) {
		try {
			expandoTableService.addDefaultTable(companyId, COMPANY_CLASSNAME);
			setSetting(columnName, value, companyId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final static String COMPANY_CLASSNAME = Company.class.getName();

}

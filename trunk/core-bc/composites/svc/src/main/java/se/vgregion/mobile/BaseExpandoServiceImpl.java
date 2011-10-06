package se.vgregion.mobile;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:32
 */
public class BaseExpandoServiceImpl {

    @Autowired
    protected ExpandoColumnLocalService expandoColumnService;

    @Autowired
    protected ExpandoTableLocalService expandoTableService;

    @Autowired
    protected ExpandoValueLocalService expandoValueService;

    public void setSetting(String targetClassName, String columnName, Object value, long companyId, int expandoType) {
        try {
            expandoValueService.addValue(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME,
                    columnName, companyId, value);
        } catch (PortalException e) {
            // If table don't exists we try to create it.
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                createExpandoTabel(targetClassName, columnName, value, companyId, expandoType);
            } else if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it.
                createExpandoColumn(targetClassName, columnName, value, companyId, expandoType);
            } else {
                throw new RuntimeException(e);
            }
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getSetting(String targetClassName, String columnName, long companyId) {

        Object value = null;
        try {
            value = expandoValueService.getData(companyId, targetClassName,
                    ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, companyId);

        } catch (PortalException e) {
            throw new RuntimeException(e);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    public List<ExpandoColumn> getAllKeys(String targetClassName, long companyId) {
        try {
            return expandoColumnService.getDefaultTableColumns(companyId, targetClassName);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String targetClassName, String columnName, long companyId) {
        try {
            expandoColumnService.deleteColumn(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createExpandoColumn(String targetClassName, String columnName, Object value, long companyId,
            int expandoType) {
		try {
			long tableId = expandoTableService.getDefaultTable(companyId, targetClassName).getTableId();
			expandoColumnService.addColumn(tableId, columnName, expandoType);
			setSetting(targetClassName, columnName, value, companyId, expandoType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void createExpandoTabel(String targetClassName, String columnName, Object value, long companyId,
            int expandoType) {
		try {
			expandoTableService.addDefaultTable(companyId, targetClassName);
			setSetting(targetClassName, columnName, value, companyId, expandoType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

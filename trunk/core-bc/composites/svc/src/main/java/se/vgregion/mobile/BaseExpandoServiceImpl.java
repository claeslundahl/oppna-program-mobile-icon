package se.vgregion.mobile;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.expando.model.ExpandoColumn;
import com.liferay.portlet.expando.model.ExpandoTable;
import com.liferay.portlet.expando.model.ExpandoTableConstants;
import com.liferay.portlet.expando.service.ExpandoColumnLocalService;
import com.liferay.portlet.expando.service.ExpandoTableLocalService;
import com.liferay.portlet.expando.service.ExpandoValueLocalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 5/10-11
 * Time: 17:32
 */
public class BaseExpandoServiceImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseExpandoServiceImpl.class);

    @Autowired
    protected ExpandoColumnLocalService expandoColumnService;

    @Autowired
    protected ExpandoTableLocalService expandoTableService;

    @Autowired
    protected ExpandoValueLocalService expandoValueService;

    public void setSetting(String targetClassName, String columnName, Object value, long companyId, long classPK, int expandoType) {
        try {
            expandoValueService.addValue(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME,
                    columnName, classPK, value);
        } catch (Exception e) {
            LOGGER.error("setSetting", e);
            createIfNeeded(companyId, targetClassName, columnName, expandoType);
            setSetting(targetClassName, columnName, value, companyId, classPK, expandoType);
        }
    }

    public Object getSetting(long companyId, String targetClassName, String columnName, long classPK) {

        Object value = null;
        try {
            value = expandoValueService.getData(companyId, targetClassName,
                    ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName, classPK);

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
            LOGGER.error("getAllKeys", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(String targetClassName, String columnName, long companyId) {
        try {
            expandoColumnService.deleteColumn(companyId, targetClassName, ExpandoTableConstants.DEFAULT_TABLE_NAME, columnName);
        } catch (Exception e) {
            LOGGER.error("delete", e);
            throw new RuntimeException(e);
        }
    }

    private ExpandoColumn createExpandoColumn(long tableId, String columnName, int expandoType) {
		try {
			return expandoColumnService.addColumn(tableId, columnName, expandoType);
		} catch (Exception e) {
            LOGGER.error("createExpandoColumn", e);
			throw new RuntimeException(e);
		}
	}

	private ExpandoTable createExpandoTable(long companyId, String targetClassName) {
        try {
			return expandoTableService.addDefaultTable(companyId, targetClassName);
		} catch (Exception e) {
            LOGGER.error("createExpandoTable", e);
			throw new RuntimeException(e);
		}
	}

    private void createIfNeeded(long companyId, String targetClassName, String columnName, int expandoType) {
        ExpandoTable expandoTable = null;
        try {
            expandoTable = expandoTableService.getDefaultTable(companyId, targetClassName);
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchTableException) {
                expandoTable = createExpandoTable(companyId, targetClassName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ExpandoTable ["+targetClassName+"]", e);
        }

        try {
            expandoColumnService.getColumn(expandoTable.getTableId(), columnName);
        } catch (PortalException e) {
            if (e instanceof com.liferay.portlet.expando.NoSuchColumnException) {
                // If column don't exists we try to create it.
                createExpandoColumn(expandoTable.getTableId(), columnName, expandoType);
            }
        } catch (SystemException e) {
            throw new RuntimeException("Failed to create ExpandoColumn ["+columnName+"]", e);
        }
    }
}

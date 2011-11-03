package se.vgregion.mobile.icon.controller;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import se.vgregion.mobile.icon.model.MobileIconPrefs;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * User: pabe
 * Date: 2011-07-25
 * Time: 09:56
 */

@Controller
@RequestMapping(value = "VIEW")
public class MobileIconController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileIconController.class);

    @ActionMapping
    public void defaultView(ActionResponse response) throws WindowStateException {
        response.setRenderParameter("p_p_state", "normal");
    }

    @RenderMapping
    public String defaultView(RenderRequest request, Model model) {
        String userId = lookupP3PInfo(request, PortletRequest.P3PUserInfos.USER_LOGIN_ID);

        MobileIconPrefs prefs = new MobileIconPrefs();
        prefs.fromPreferences(request);

        String target = prefs.getTarget("url");
        model.addAttribute("target", target);
        if ("url".equals(target)) {
            String targetUrl = prefs.getTargetUrl("");
            model.addAttribute("targetUrl", targetUrl);
        } else if ("widgetUrl".equals(target)) {
            String widgetUrl = prefs.getWidgetUrl();
            model.addAttribute("widgetUrl", widgetUrl);
        }

        model.addAttribute("title", prefs.getTitle("untitled"));

        String counterService = prefs.getCounterService(null);
        if (counterService != null) {
            String cntResult = getCount(counterService, userId, 300);
            if (StringUtils.isNotBlank(cntResult))
                model.addAttribute("count", cntResult);
        }

        model.addAttribute("iconStyle", prefs.getIconStyle("mobile-none"));
        
        model.addAttribute("updateInterval", prefs.getUpdateInterval("100000000"));

        return "icon";
    }

    @ResourceMapping
    public void getCount(ResourceRequest request, ResourceResponse response) throws IOException {
        String userId = lookupP3PInfo(request, PortletRequest.P3PUserInfos.USER_LOGIN_ID);

        MobileIconPrefs prefs = new MobileIconPrefs();
        prefs.fromPreferences(request);

        String counterService = prefs.getCounterService(null);
        if (counterService != null) {
            PrintWriter writer = response.getWriter();
            writer.write(getCount(counterService, userId, 10000));
            writer.close();
        }
    }

    private String getCount(String counterService, String userId, int timeoutMillis) {
        Message message = new Message();
        message.setPayload(userId == null ? "" : userId);

        Object response;
        try {
            response = MessageBusUtil.sendSynchronousMessage(counterService, message, timeoutMillis);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            response = "-";
        }

        if (response instanceof String) {
            return response.toString();
        } else {
            if (response instanceof Exception) {
                ((Exception) response).printStackTrace();
            }
            return "-";
        }
    }

    private String lookupP3PInfo(PortletRequest req, PortletRequest.P3PUserInfos p3pInfo) {
        Map<String, String> userInfo = (Map<String, String>) req.getAttribute(PortletRequest.USER_INFO);
        String info;
        if (userInfo != null) {
            info = userInfo.get(p3pInfo.toString());
        } else {
            return null;
        }
        return info;
    }
}

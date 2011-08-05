package se.vgregion.mobileicon.controller;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import javax.portlet.*;
import java.io.IOException;
import java.io.PrintWriter;

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
        PortletPreferences preferences = request.getPreferences();

        String imageId = preferences.getValue("imageId", null);
        String title = preferences.getValue("title", "untitled");
        String counterService = preferences.getValue("counterService", null);
        String updateInterval = preferences.getValue("updateInterval", "100000000");

        String target = preferences.getValue("target", "url");
        model.addAttribute("target", target);
        if ("url".equals(target)) {
            String targetUrl = preferences.getValue("targetUrl", "");
            model.addAttribute("targetUrl", targetUrl);
        }

        model.addAttribute("title", title);
        model.addAttribute("imageId", imageId);

        if (counterService != null) {
            model.addAttribute("count", getCount(counterService, 300));
        }
        
        model.addAttribute("updateInterval", updateInterval);

        return "icon";
    }

    @ResourceMapping
    public void getCount(ResourceRequest request, ResourceResponse response) throws IOException {
        PortletPreferences preferences = request.getPreferences();
        String counterService = preferences.getValue("counterService", null);
        if (counterService != null) {
            PrintWriter writer = response.getWriter();
            writer.write(getCount(counterService, 2000));
            writer.close();
        }
    }

    private String getCount(String counterService, int timeoutMillis) {
        Message message = new Message();
        message.setPayload(" ");

        Object response;
        try {
            response = MessageBusUtil.sendSynchronousMessage(counterService, message, timeoutMillis);
        } catch (Exception e) {
            LOGGER.warn("No response.", e);
            response = "-";
        }

        if (response != null) {
            return response.toString();
        } else {
            return "-";
        }
    }

    @ActionMapping(params = "action=showWidget")
    public void showWidget(ActionRequest request, ActionResponse response) throws WindowStateException {
        response.setRenderParameter("action", "showWidget");
        response.setRenderParameter("p_p_state", "exclusive");
    }

    @RenderMapping(params = "action=showWidget")
    public String showWidget(RenderRequest request, RenderResponse response, Model model) {

        PortletPreferences preferences = request.getPreferences();
        String widgetScript = preferences.getValue("widgetScript", "");
        model.addAttribute("widgetScript", widgetScript);

        return "widget";
    }
}

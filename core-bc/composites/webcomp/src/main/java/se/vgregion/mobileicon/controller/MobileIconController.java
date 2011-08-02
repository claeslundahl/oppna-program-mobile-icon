package se.vgregion.mobileicon.controller;

import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBusException;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.*;

/**
 * User: pabe
 * Date: 2011-07-25
 * Time: 09:56
 */

@Controller
@RequestMapping(value = "VIEW")
public class MobileIconController {

    @ActionMapping
    public void defaultView(ActionResponse response) throws WindowStateException {
        response.setRenderParameter("p_p_state", "normal");
    }

    @RenderMapping
    public String defaultView(RenderRequest request, Model model) {
        PortletPreferences preferences = request.getPreferences();

        String imageId = preferences.getValue("imageId", null);
        String title = preferences.getValue("title", "untitled");

        String target = preferences.getValue("target", "url");
        model.addAttribute("target", target);
        if ("url".equals(target)) {
            String targetUrl = preferences.getValue("targetUrl", "");
            model.addAttribute("targetUrl", targetUrl);
        }

        model.addAttribute("title", title);
        model.addAttribute("imageId", imageId);

        Message message = new Message();
        message.setPayload("");

        Object response = null;
        try {
            response = MessageBusUtil.sendSynchronousMessage("vgr/test-counter", message, 7000);
        } catch (MessageBusException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//        return ControllerUtil.extractResponse(response, createUserJaxbUtil);


        return "icon";
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

package se.vgregion.mobile.settings.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Created by IntelliJ IDEA.
 * User: david
 * Date: 28/9-11
 * Time: 20:53
 */
@Controller
@RequestMapping("VIEW")
public class MobileSettingsController {

    @RenderMapping
    public String defaultView() {
        return "view";
    }
}

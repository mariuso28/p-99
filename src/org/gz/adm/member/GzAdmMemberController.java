package org.gz.adm.member;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.services.GzServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser"})

@RequestMapping("/admMember")
public class GzAdmMemberController {

	private static Logger log = Logger.getLogger(GzAdmMemberController.class);
	@Autowired
	private GzServices gzServices;

	@RequestMapping(value="/accountDetails", method = RequestMethod.GET)
	public ModelAndView accountDetails(ModelMap model)
	{
		log.info("in accountDetails");
		AdmMemberForm admMemberForm = new AdmMemberForm();
		return new ModelAndView("admMemberDetails","admMemberForm",admMemberForm);
	}
	
	@RequestMapping(value="/processAdmMember", params="memberCancel", method = RequestMethod.POST)
	public Object memberCancel(ModelMap model)
	{
		GzBaseUser currUser = (GzBaseUser) model.get("currUser");
		if (currUser.getRole().equals(GzRole.ROLE_ADMIN))
			return "redirect:/rp/adm/backToAdm";
		return null;
	}
	
}

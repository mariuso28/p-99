package org.gz.adm;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.gz.baseuser.GzBaseUser;
import org.gz.baseuser.GzRole;
import org.gz.home.persistence.GzPersistenceException;
import org.gz.services.GzServices;
import org.gz.web.admin.GzAdminController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes({"currUser"})

@RequestMapping("/adm")
public class GzAdmController {

	private static Logger log = Logger.getLogger(GzAdminController.class);
	@Autowired
	private GzServices gzServices;
	
	//  /gz/adm/logon?user&email=
		@RequestMapping(value = "/logon", params="user", method = RequestMethod.GET)
		public Object signin(String memberId,ModelMap model,HttpServletRequest request) {
			
			log.info("Received request to logon");
				
			GzBaseUser currUser = null;
		
			try {
				currUser = gzServices.getGzHome().getBaseUserByMemberId(memberId);
			} catch (GzPersistenceException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				return backToSignin(model,"Unexpected error on Admin - contact support");
			}
		
			model.addAttribute("currUser",currUser);
			
			
			HttpSession session = request.getSession();						// have to set in the session as end up in other controllers
			log.trace("Setting session attribute : sCurrUser : " +  currUser );
			session.setAttribute("sCurrUser", currUser);	
			
			return goAdmHome("","",model);
		}
	
		private Object goAdmHome(String errMsg,String infoMsg,ModelMap model){
			
			GzMemberForm admForm = createMemberForm(model);
			admForm.setInfoMsg(infoMsg);
			return new ModelAndView("admHome","admForm", admForm);
		}
		
		@RequestMapping(value = "/registerMember", method = RequestMethod.GET)
		public ModelAndView registerMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(model);
			
			return new ModelAndView("admMemberRegister","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/manageMember", method = RequestMethod.GET)
		public ModelAndView manageMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(model);
			
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/manageMemberFromTree", method = RequestMethod.GET)
		public ModelAndView manageMemberFromTree(ModelMap model,String name)
		{
			GzMemberForm memberForm = createMemberForm(model);
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(name);
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				memberForm.getInCompleteCommand().setEnabled(bu.isEnabled());
				DecimalFormat df1 = new DecimalFormat("##0.00");
				memberForm.getInCompleteCommand().setWinCommission(df1.format(bu.getAccount().getWinCommission()));
				memberForm.getInCompleteCommand().setBetCommission(df1.format(bu.getAccount().getBetCommission()));
				memberForm.getInCompleteCommand().setCredit(df1.format(bu.getAccount().getCredit()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error managing member - contact support.");
				return new ModelAndView("admMemberManage","memberForm", memberForm);
			}
			
			return new ModelAndView("admMemberManage","memberForm", memberForm);
		}
		
		@RequestMapping(value = "/activateMember", method = RequestMethod.GET)
		public ModelAndView activateMember(ModelMap model)
		{
			GzMemberForm memberForm = createMemberForm(model);
			return setActivated(memberForm);
		}
		
		private ModelAndView setActivated(GzMemberForm memberForm) {
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByCode(memberForm.getInCompleteCommand().getMemberToChangeCode());
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				memberForm.getInCompleteCommand().setEnabled(bu.isEnabled());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error activating member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			return new ModelAndView("admMemberActivate","memberForm", memberForm);
		}

		@RequestMapping(value = "/processAdm", params="changeMemberActive", method = RequestMethod.POST)
		public ModelAndView changeMemberActive(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			memberForm = createMemberForm(model);
			memberForm.setInCompleteCommand(command);
			return setActivated(memberForm);
		}
		
		private GzMemberForm createMemberForm(ModelMap model)
		{
			GzMemberForm memberForm = new GzMemberForm();
			GzBaseUser user = (GzBaseUser) model.get("currUser");
			
			memberForm.setMemberSummary(populateMemberSummary(user,null,memberForm.getFlatMembers()));
			
			
			memberForm.setRoles(user.getRole().getAvailableRoles());
			memberForm.setInCompleteCommand(new GzMemberCommand());
			DecimalFormat df1 = new DecimalFormat("##0.00");
			memberForm.getInCompleteCommand().setRole(memberForm.getRoles().get(0));
			memberForm.getInCompleteCommand().setWinCommission(df1.format(user.getAccount().getWinCommission()));
			memberForm.getInCompleteCommand().setBetCommission(df1.format(user.getAccount().getBetCommission()));
			memberForm.getInCompleteCommand().setCredit(df1.format(user.getAccount().getCredit()));		
			memberForm.getInCompleteCommand().setEnabled(user.isEnabled());
			return memberForm;
		}
		
		private GzMemberSummary populateMemberSummary(GzBaseUser user, GzMemberSummary parent, List<GzMemberSummary> flatMembers) {
			GzMemberSummary ms = new GzMemberSummary(user);
			ms.setParent(parent);
			
			if (!user.getRole().equals(GzRole.ROLE_ADMIN))
				flatMembers.add(ms);
			if (user.getRole().equals(GzRole.ROLE_PLAY))
				return ms;
			
			gzServices.getGzHome().getDownstreamForParent(user);
			for (GzBaseUser bu : user.getMembers())
				ms.getMembers().add(populateMemberSummary(bu,ms,flatMembers));
			return ms;
		}

		
		private ModelAndView backToSignin(ModelMap model,String errMsg) {
			
			log.info("Received request to signin");
				
			HashMap<String,String> logon = new HashMap<String,String>();
			logon.put("errMsg", errMsg);
			logon.put("infoMsg", "");
			logon.put("email", "");
			
			return new ModelAndView("logon","logon", logon);
		}
		
		
		@RequestMapping(value="/processAdm", params="activateMember", method = RequestMethod.POST)
		public ModelAndView activateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("activateMember: " + command);
			return acivate(true,model,command);
		}
		
		@RequestMapping(value="/processAdm", params="deactivateMember", method = RequestMethod.POST)
		public ModelAndView deactivateMember(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();
			
			log.info("deactivateMember: " + command);
			return acivate(false,model,command);
		}
		
		private ModelAndView acivate(boolean activate,ModelMap model,GzMemberCommand command)
		{
			GzMemberForm memberForm = createMemberForm(model);
			memberForm.setInCompleteCommand(command);
			try
			{
				GzBaseUser bu = gzServices.getGzHome().getBaseUserByEmail(command.getMemberToChangeCode());
				if (bu == null)
				{
					throw new GzPersistenceException("Member does not exist");
				}
				bu.setEnabled(activate);
				gzServices.getGzHome().updateBaseUserProfile(bu);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm.setErrMsg("Error activating/deactivating member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			memberForm.getInCompleteCommand().setEnabled(activate);
			memberForm.setInfoMsg("Member : " + command.getMemberToChangeCode() + " - Successfully " + (activate ? "activated." : " deactivated."));
			return new ModelAndView("admMemberActivate","memberForm", memberForm);
		}
		
		@RequestMapping(value="/backToAdm", method = RequestMethod.GET)
		public Object backToAdm(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberCancel", method = RequestMethod.POST)
		public Object memberCancel(ModelMap model)
		{
			return goAdmHome("","",model);
		}
		
		@RequestMapping(value="/processAdm", params="memberRegister", method = RequestMethod.POST)
		public Object memberRegister(@ModelAttribute("memberForm") GzMemberForm memberForm,ModelMap model)
		{
			GzMemberCommand command = memberForm.getCommand();	
			log.info("memberRegister: " + command);
			GzBaseUser superior = (GzBaseUser) model.get("currUser");
			GzRole role = command.getRole();
			
			command.setvPassword("88888888");
			command.getProfile().setPassword("88888888");
			
			String errMsg = command.getProfile().validate(command.getvPassword(),role);
			if (!errMsg.isEmpty())
			{
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg(errMsg);
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			
			GzBaseUser newMember = null;
			try
			{
				newMember = (GzBaseUser) role.getCorrespondingClass().newInstance();
								
				newMember.copyProfileValues(command.getProfile());
				newMember.setParent(superior);
				newMember.setParentCode(superior.getCode());
				newMember.setRole(role);
				newMember.setEnabled(true);
				PasswordEncoder encoder = new BCryptPasswordEncoder();
				newMember.setPassword(encoder.encode(command.getvPassword()));
				
				gzServices.getGzHome().storeBaseUser(newMember);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				memberForm = createMemberForm(model);
				memberForm.setInCompleteCommand(command);
				memberForm.setErrMsg("Error registering member - contact support.");
				return new ModelAndView("admMemberRegister","memberForm", memberForm);
			}
			
			return goAdmHome("","Member : " +  newMember.getContact() + " successfully registered with memberId : " + newMember.getMemberId(),model);
		}
		
		

		
}

package org.gz.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.gz.baseuser.GzRole;

public class GzMemberForm implements Serializable {

	private static final long serialVersionUID = -9110917883160354671L;
	private GzMemberCommand command;
	private GzMemberCommand inCompleteCommand;
	private boolean adminOnly;
	private String errMsg;
	private String infoMsg;
	private List<GzRole> roles; 
	private List<GzMemberSummary> flatMembers = new ArrayList<GzMemberSummary>();
	private GzMemberSummary memberSummary;
	
	public GzMemberForm()
	{
	}

	public GzMemberCommand getCommand() {
		return command;
	}

	public void setCommand(GzMemberCommand command) {
		this.command = command;
	}

	public GzMemberCommand getInCompleteCommand() {
		return inCompleteCommand;
	}

	public void setInCompleteCommand(GzMemberCommand inCompleteCommand) {
		this.inCompleteCommand = inCompleteCommand;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public boolean isAdminOnly() {
		return adminOnly;
	}

	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	public GzMemberSummary getMemberSummary() {
		return memberSummary;
	}

	public void setMemberSummary(GzMemberSummary memberSummary) {
		this.memberSummary = memberSummary;
	}

	public List<GzRole> getRoles() {
		return roles;
	}

	public void setRoles(List<GzRole> roles) {
		this.roles = roles;
	}

	public List<GzMemberSummary> getFlatMembers() {
		return flatMembers;
	}

	public void setFlatMembers(List<GzMemberSummary> flatMembers) {
		this.flatMembers = flatMembers;
	}


}

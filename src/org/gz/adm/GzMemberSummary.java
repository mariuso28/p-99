package org.gz.adm;

import java.util.ArrayList;
import java.util.List;

import org.gz.baseuser.GzBaseUser;

public class GzMemberSummary {

	private String memberId;
	private String contact;
	private String rank;
	private GzMemberSummary parent;
	private List<GzMemberSummary> members = new ArrayList<GzMemberSummary>();
	private boolean enabled;
	private double betCommission;
	private double winCommission;
	private double credit;
	
	public GzMemberSummary() {
	}
	
	public GzMemberSummary(GzBaseUser bu)
	{
		setMemberId(bu.getContact());
		setMemberId(bu.getMemberId());
		setRank(bu.getRole().getShortCode());
		if (bu.getParent()!=null)
			setParent(new GzMemberSummary(bu.getParent()));
		setEnabled(bu.isEnabled());
		setBetCommission(bu.getAccount().getBetCommission());
		setWinCommission(bu.getAccount().getWinCommission());
		setCredit(bu.getAccount().getCredit());
	}
	
	public boolean getHasPeer()
	{
		if (parent==null)
			return false;
		if (this.equals(parent.members.get(parent.members.size()-1)))
			return false;
		return true;
	}

	public String getRankLongName()
	{
		if (rank.equals("Admin"))
			return "Administrator";
		if (rank.equals("SMA"))
			return "Senior Master Agent";
		if (rank.equals("MA"))
			return "Master Agent";
		return rank;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public GzMemberSummary getParent() {
		return parent;
	}

	public void setParent(GzMemberSummary parent) {
		this.parent = parent;
	}

	public List<GzMemberSummary> getMembers() {
		return members;
	}

	public void setMembers(List<GzMemberSummary> members) {
		this.members = members;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public double getBetCommission() {
		return betCommission;
	}

	public void setBetCommission(double betCommission) {
		this.betCommission = betCommission;
	}

	public double getWinCommission() {
		return winCommission;
	}

	public void setWinCommission(double winCommission) {
		this.winCommission = winCommission;
	}

	public double getCredit() {
		return credit;
	}

	public void setCredit(double credit) {
		this.credit = credit;
	}

	
	
}

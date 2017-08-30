package org.gz.baseuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import org.gz.admin.GzAdmin;
import org.gz.agent.GzAgent;
import org.gz.dustbin.GzDustbin;

public enum GzRole  implements Serializable{
		
		ROLE_PLAY("Player",0,'p',"py",GzBaseUser.class,GzRoleType.PLAYER,"FFD6D6"),
		ROLE_AGENT1("Agent-1",1,'a',"a1",GzAgent.class,GzRoleType.AGENT,"FFF7D6"),
		ROLE_AGENT2("Agent-2",2,'b',"a2",GzAgent.class,GzRoleType.AGENT,"E9FFD6"),
		ROLE_AGENT3("Agent-3",3,'c',"a3",GzAgent.class,GzRoleType.AGENT,"D6FFEE"),
		ROLE_AGENT4("Agent-4",4,'d',"a4",GzAgent.class,GzRoleType.AGENT,"D6ECFF"),
		ROLE_AGENT5("Agent-5",5,'e',"a5",GzAgent.class,GzRoleType.AGENT,"E3D6FF"),
		ROLE_AGENT6("Agent-6",6,'f',"a6",GzAgent.class,GzRoleType.AGENT,"D6FFEE"),
		ROLE_AGENT7("Agent-7",7,'g',"a7",GzAgent.class,GzRoleType.AGENT,"D6ECFF"),
		ROLE_AGENT8("Agent-8",8,'h',"a8",GzAgent.class,GzRoleType.AGENT,"E3D6FF"),
		ROLE_AGENT9("Agent-9",9,'i',"a9",GzAgent.class,GzRoleType.AGENT,"E3D6FF"),
		ROLE_DUSTBINA("Dustbin-A",10,'A',"dbA",GzDustbin.class,GzRoleType.DUSTBIN,"F7D6FF"),
		ROLE_DUSTBINB("Dustbin-B",11,'B',"dbB",GzDustbin.class,GzRoleType.DUSTBIN,"F7D6FF"),
		ROLE_DUSTBINC("Dustbin-C",12,'C',"dbC",GzDustbin.class,GzRoleType.DUSTBIN,"F7D6FF"),
		ROLE_ADMIN("Admin",13,'x',"adm",GzAdmin.class,GzRoleType.ADMIN,"F7D6FF");
		
		private static final Logger log = Logger.getLogger(GzRole.class);
		private String desc;
		private int rank;					// don't use ordinal cos we might mess with the enum order
		private Character code;
		private String shortCode;
		@SuppressWarnings("rawtypes")
		private Class correspondingClass;
		private String color;
		private GzRoleType type;
		private static HashMap<Character,GzRole> codeMap;
		
		@SuppressWarnings("rawtypes")
		private GzRole(String desc,int rank,char code,String shortCode,Class correspondingClass,GzRoleType type,String color)
		{
			setRank(rank);
			setDesc(desc);
			setCode(code);
			setColor(color);
			setCorrespondingClass(correspondingClass);
			setShortCode(shortCode);
			setType(type);
			GzRole.addCodeMap(this,code);
		}
		
		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		private static void addCodeMap(GzRole role,Character code)
		{
			if (codeMap==null)									// static initialization dont work
				codeMap=new HashMap<Character,GzRole>();
			codeMap.put(code, role);
		}
		
		public static GzRole getRoleForCode(String code)
		{
			Character ch = '?';
			for (int index=code.length()-1; index>=0; index--)
			{
				ch = code.charAt(index);
				if (!Character.isDigit(ch))
					break;
			}
			return codeMap.get(ch);
		}
		
		public static GzRole getRoleForRank(int rank)
		{
			for (GzRole role : GzRole.codeMap.values())
			{
				if (role.rank==rank)
					return role;
			}
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		public static Class getRoleClassForCode(String code)
		{
			GzRole role = getRoleForCode(code);
			return role.getCorrespondingClass();
		}
		
		private void setDesc(String desc)
		{
			this.desc = desc;
		}
		
		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getRank() {
			return rank;
		}

		public String getDesc()
		{
			return desc;
		}
		
		public void setCode(Character code) {
			this.code = code;
		}

		public Character getCode() {
			return code;
		}

		public void setShortCode(String shortCode) {
			this.shortCode = shortCode;
		}

		public String getShortCode() {
			return shortCode;
		}
		
		public GzRoleType getType() {
			return type;
		}

		public void setType(GzRoleType type) {
			this.type = type;
		}

		public void setCorrespondingClass(@SuppressWarnings("rawtypes") Class correspondingClass) {
			this.correspondingClass = correspondingClass;
		}

		@SuppressWarnings("rawtypes")
		public Class getCorrespondingClass() {
			return correspondingClass;
		}

		public static Logger getLog() {
			return log;
		}
		
		
		public List<GzRole> getAllRoles()
		{
			List<GzRole> roles = new ArrayList<GzRole>();
			for (int r = rank; r>=0; r--)
				roles.add(getRoleForRank(r));
			return roles;
		}

		public List<GzRole> getAvailableRoles()
		{
			List<GzRole> roles = new ArrayList<GzRole>();
			if (this.equals(GzRole.ROLE_PLAY))
				return roles;
			
			if (this.getType().equals(GzRoleType.ADMIN))
			{
				roles.add(GzRole.ROLE_DUSTBINA);
				return roles;
			}
			if (this.getType().equals(GzRoleType.DUSTBIN))
			{
				for (int r = rank-1; r>=1; r--)
				{
					roles.add(getRoleForRank(r));
				}
			}
			if (this.getType().equals(GzRoleType.AGENT))
			{
				for (int r = rank-1; r>=0; r--)
				{
					roles.add(getRoleForRank(r));
				}
			}
			return roles;
		}
		
}

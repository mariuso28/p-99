package org.gz.json;

public enum GzGameType {
	// D2(2,"2 Digit 1st Prize Only",2),
	ABCA(7,"3D 1st Prize Only",3),
	ABCC(8,"3D 1st/2nd/3rd Prizes",3),		
	D4Big(1,"4D 1st/2nd/3rd/Special/Consolation Prizes",4),  
	D4Small(2,"4D 1st/2nd/3rd Prizes",4), 					
	D4IBoxBig(3,"4D - IBox 1st/2nd/3rd/Special/Consolation Prizes",4),  
	D4IBoxSmall(4,"4D - IBox 1st/2nd/3rd Prizes",4), 
	D4BoxBig(5,"4D - Box 1st/2nd/3rd/Special/Consolation Prizes",4),
	D4BoxSmall(6,"4D - Box 1st/2nd/3rd Prizes",4);
	
	private int index;
	private String desc;
	private int digits;
	
	GzGameType(int index,String desc,int digits)
	{
		setIndex(index);
		setDesc(desc);
		setDigits(digits);
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public int getDigits() {
		return digits;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}


}

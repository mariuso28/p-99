package org.gz.account;

import java.util.Date;

public class GzInvoice extends GzDeposit {
	
	private static final long serialVersionUID = -4558471539417421128L;
	
	public static final char STATUSOPEN = 'O';
	public static final char STATUSCLOSED = 'C';
	public static final char STATUSSETTLED = 'S';
	
	private double commission;
	private double netAmount;
	private long parentId;
	private Date dueDate;
	private long paymentId;
	private char status;
	
	public GzInvoice()
	{
	}

	public GzInvoice(String payer,String payee,double amount,double commission,double netAmount,Date timestamp,Date dueDate)
	{
		setPayer(payer);
		setPayee(payee);
		setType(GzXaction.XTYPEINVOICE);
		setAmount(amount);
		setTimestamp(timestamp);
		setDueDate(dueDate);
		setCommission(commission);
		setNetAmount(netAmount);
		setPaymentId(-1L);
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(long paymentId) {
		this.paymentId = paymentId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public void update(double amount2, double commission2, double netAmount2,double stake2,char winstake) {
		amount += amount2;
		commission += commission2;
		netAmount += netAmount2;
	}

	
}

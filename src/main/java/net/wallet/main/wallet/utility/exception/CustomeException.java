package net.wallet.main.wallet.utility.exception;

public class CustomeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object obj;
	
	public CustomeException(String message,Object obj) {
		super(message);
		
		this.obj=obj;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	

}
package com.jyd.exception;
/***
 * 业务逻辑异常
 *
 *@author 刘声凤
 *@date 2009-5-4 下午10:08:01
 */
public class BizException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public BizException(){
	}
	public BizException(String message){
		super(message);
	}
	public BizException(String message, Throwable cause){
		super(message,cause);
	}
	public BizException(Throwable cause){
		super(cause);
	}
	@Override
	public String getMessage() {
		return super.getMessage();
	}
}

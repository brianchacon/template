package com.ich.template.exception;

public class BadRequestException  extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codigo;	

	private String textStatus;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return textStatus;
	}

	public void setMensaje(String mensaje) {
		this.textStatus = mensaje;
	}

	public BadRequestException(String codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.textStatus = mensaje;
	}
	
	
	
	
}

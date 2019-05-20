package com.bridgeit.exceptions;

public class NoteException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public  NoteException(String msg) {
		super(msg);
	}
}

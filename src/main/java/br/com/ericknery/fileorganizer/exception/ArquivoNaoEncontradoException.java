package br.com.ericknery.fileorganizer.exception;

public class ArquivoNaoEncontradoException extends Exception{

	private static final long serialVersionUID = 1L;
	public ArquivoNaoEncontradoException(String msg)
	{
		super(msg);
	}
}

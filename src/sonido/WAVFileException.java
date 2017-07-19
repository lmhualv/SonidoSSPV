package sonido;

//plantilla para Excepciones

public class WAVFileException extends Exception
{
	public WAVFileException()
	{
		super();
	}

	public WAVFileException(String message)
	{
		super(message);
	}

	public WAVFileException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public WAVFileException(Throwable cause) 
	{
		super(cause);
	}
}

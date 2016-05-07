
public class DictionaryException extends RuntimeException{
	//creates an exception object using the constructor of RuntimeException (NOTE: error does not print specifics as it is caught many times in other files)
	public DictionaryException(String empty){
		super(empty);
	}
	
}

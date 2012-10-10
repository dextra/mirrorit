package mirrorit;

public class MirroritHttpCodeException extends MirroritException {

	private static final long serialVersionUID = 8948989536983986461L;

	private final String msg;
	private final int code;

	public MirroritHttpCodeException(int code) {
		this(code, null);
	}

	public MirroritHttpCodeException(int code, String msg) {
		this(code, msg, null);
	}

	public MirroritHttpCodeException(int code, String msg, Throwable e) {
		super("HttpCode: " + code + " " + msg, e);
		this.code = code;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

}

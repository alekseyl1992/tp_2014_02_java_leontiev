package messaging;
import frontend.FrontendServlet;

public class MsgUpdateUserId extends MsgToFS {
	private String sessionId;
	private Long id;
	
	public MsgUpdateUserId(Address from, Address to, String sessionId, Long id) {
		super(from, to);
		this.sessionId = sessionId;
		this.id = id;
	}

	void exec(FrontendServlet frontend) {
		frontend.setId(sessionId, id);
	}
}

package messaging;

import server.AccountService;
import server.DBException;

public class MsgLoginUser extends MsgToAS {
	private String login;
    private String password;
    private String sessionId;
	
	public MsgLoginUser(Address from, Address to, String login, String password, String sessionId) {
		super(from, to);
		this.login = login;
        this.password = password;
        this.sessionId = sessionId;
	}

	void exec(AccountService accountService) {
        try {
            Long id = accountService.tryLogin(login, password);
            accountService.getMessageSystem()
                    .sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
        }
        catch (DBException e) {
            accountService.getMessageSystem()
                    .sendMessage(new MsgDBError(getTo(), getFrom(), sessionId));
        }
	}
}

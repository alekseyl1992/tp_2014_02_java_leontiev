package messaging;

import server.AccountService;
import server.DBException;

public class MsgRegisterUser extends MsgToAS {
	private String login;
    private String password;
    private String email;
    private String sessionId;

	public MsgRegisterUser(Address from, Address to, String login, String password, String email, String sessionId) {
		super(from, to);
		this.login = login;
        this.password = password;
        this.email = email;
        this.sessionId = sessionId;
	}

	void exec(AccountService accountService) {
        try {
            Long id = accountService.tryRegister(login, password, email);
            accountService.getMessageSystem()
                    .sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
        }
        catch (DBException e) {
            accountService.getMessageSystem()
                    .sendMessage(new MsgDBError(getTo(), getFrom(), sessionId));
        }
	}
}
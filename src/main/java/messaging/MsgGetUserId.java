package messaging;

import server.AccountService;

public class MsgGetUserId extends MsgToAS {
	private String login;
    private String password;
    private String sessionId;
	
	public MsgGetUserId(Address from, Address to, String login, String password, String sessionId) {
		super(from, to);
		this.login = login;
        this.password = password;
        this.sessionId = sessionId;
	}

	void exec(AccountService accountService) {
		Long id = accountService.getUserId(login, password);
		accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(), getFrom(), sessionId, id));
	}
}

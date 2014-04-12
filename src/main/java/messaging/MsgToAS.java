package messaging;
import server.AccountService;

public abstract class MsgToAS extends Msg {

	public MsgToAS(Address from, Address to) {
		super(from, to);		
	}

	void exec(Subscriber subscriber) {
		if(subscriber instanceof AccountService){
			exec((AccountService) subscriber);
		}
	}

	abstract void exec(AccountService accountService);
}

package messaging;
import frontend.FrontendServlet;

public abstract class MsgToFS extends Msg {

	public MsgToFS(Address from, Address to) {
		super(from, to);
	}

	public void exec(Subscriber subscriber) {
		if(subscriber instanceof FrontendServlet){
			exec((FrontendServlet)subscriber);
		}
	}
	
	abstract void exec(FrontendServlet frontend);
}

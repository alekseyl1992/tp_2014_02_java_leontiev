package frontend;

import resourcing.resources.Resource;
import java.io.Serializable;

public class Locations implements Resource, Serializable {
    public static final String INDEX = "/index";
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String POLL = "/poll";
    public static final String TIMER = "/timer";
    public static final String REGISTRATION = "/registration";
}

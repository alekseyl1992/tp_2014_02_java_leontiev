package messaging;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AddressServiceTest {
    Address asAddress, fsAddress;

    @Before
    public void setUp() {
        asAddress = new Address();
        fsAddress = new Address();
    }

    @Test
    public void testGetAccountService() throws Exception {
        AddressService addressService = new AddressService();
        assertTrue(addressService.getAccountService() == null);
    }

    @Test
    public void testSetAccountService() throws Exception {
        AddressService addressService = new AddressService();
        addressService.setAccountService(asAddress);
        assertTrue(addressService.getAccountService() == asAddress);
    }

    @Test
    public void testGetFrontendServlet() throws Exception {
        AddressService addressService = new AddressService();
        assertTrue(addressService.getFrontendServlet() == null);
    }

    @Test
    public void testSetFrontendServlet() throws Exception {
        AddressService addressService = new AddressService();
        addressService.setFrontendServlet(fsAddress);
        assertTrue(addressService.getFrontendServlet() == fsAddress);
    }
}

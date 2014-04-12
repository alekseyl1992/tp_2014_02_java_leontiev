package messaging;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AddressTest {
    @Test
    public void testHashCode() throws Exception {
        Address address1 = new Address();
        Address address2 = new Address();
        assertTrue(address2.hashCode() - address1.hashCode() == 1);
    }
}

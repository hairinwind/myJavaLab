package my.javalab.spring.tx;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(locations = "classpath:spring.xml")
public class TransactionalServiceTest {

	@Test
	public void testCreateCustomer() {
		fail("Not yet implemented");
	}

}

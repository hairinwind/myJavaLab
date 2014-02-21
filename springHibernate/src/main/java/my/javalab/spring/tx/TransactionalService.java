package my.javalab.spring.tx;

import my.javalab.spring.domain.Customer;

public class TransactionalService {
	
	public void createCustomer(Customer customer) {
		System.out.println("running...");
	}
}

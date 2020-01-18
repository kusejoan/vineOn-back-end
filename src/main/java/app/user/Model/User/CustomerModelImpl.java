package app.user.Model.User;


import app.user.Entity.Customer;
import app.user.Repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerModelImpl implements CustomerModel {
    public CustomerModelImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private CustomerRepository customerRepository;
    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public void save(Customer user) {
        customerRepository.save(user);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}

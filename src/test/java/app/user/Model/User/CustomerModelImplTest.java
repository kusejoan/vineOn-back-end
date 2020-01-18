/*
 * Copyright (c) 2020.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *  3. Neither the name of Vineon nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 */

package app.user.Model.User;

import app.user.Entity.Customer;
import app.user.Entity.User;
import app.user.Repo.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomerModelImplTest {

    @Mock
    private CustomerRepository mockCustomerRepository;

    private CustomerModelImpl customerModelImplUnderTest;

    @Before
    public void setUp() {
        initMocks(this);
        customerModelImplUnderTest = new CustomerModelImpl(mockCustomerRepository);
    }

    @Test
    public void testFindByUsername() {
        // Setup
        final Customer expectedResult = new Customer(new User("username", "password", "role"));

        // Configure CustomerRepository.findByUsername(...).
        final Customer customer = new Customer(new User("username", "password", "role"));
        when(mockCustomerRepository.findByUsername("username")).thenReturn(customer);

        // Run the test
        final Customer result = customerModelImplUnderTest.findByUsername("username");

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSave() {
        // Setup
        final Customer user = new Customer(new User("username", "password", "role"));

        // Configure CustomerRepository.save(...).
        final Customer customer = new Customer(new User("username", "password", "role"));
        when(mockCustomerRepository.save(new Customer(new User("username", "password", "role")))).thenReturn(customer);

        // Run the test
        customerModelImplUnderTest.save(user);

        // Verify the results
    }

    @Test
    public void testFindAll() {
        // Setup
        final List<Customer> expectedResult = Arrays.asList(new Customer(new User("username", "password", "role")));

        // Configure CustomerRepository.findAll(...).
        final List<Customer> customers = Arrays.asList(new Customer(new User("username", "password", "role")));
        when(mockCustomerRepository.findAll()).thenReturn(customers);

        // Run the test
        final List<Customer> result = customerModelImplUnderTest.findAll();

        // Verify the results
        assertEquals(expectedResult, result);
    }
}

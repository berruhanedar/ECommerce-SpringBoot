package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.CustomerMapper;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // INSTANCE gerek yok

    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(CustomerMapper.INSTANCE::toDTO)
                .toList();
    }

    public CustomerDTO getCustomerById(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return CustomerMapper.INSTANCE.toDTO(customer);
    }

    public CustomerDTO createCustomer(NewCustomerRequestDTO dto) {
        Customer customer = CustomerMapper.INSTANCE.toEntity(dto);
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.INSTANCE.toDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(UpdateCustomerRequestDTO dto) {
        Customer existingCustomer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + dto.getCustomerId()));
        Customer updatedCustomer = CustomerMapper.INSTANCE.toEntity(dto);
        updatedCustomer.setCustomerId(existingCustomer.getCustomerId());
        customerRepository.save(updatedCustomer);
        return CustomerMapper.INSTANCE.toDTO(updatedCustomer);

        /**
         * Ilk database'den id ile mevcut existingCustomer'ı cekiyoruz.
         * Sonra gelen dto ile existingCustomer'ı update ediyoruz.
         * Sonra updatedCustomer'ı save ediyoruz.
         * Sonra updatedCustomer'ı DTO'ya cevirip return ediyoruz.
         */
    }

    public void deleteCustomer(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }
}

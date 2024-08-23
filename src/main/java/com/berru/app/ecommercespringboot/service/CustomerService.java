package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.CustomerMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;

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
        customerRepository.save(customer);
        return CustomerMapper.INSTANCE.toDTO(customer);
    }

    public CustomerDTO updateCustomer(UpdateCustomerRequestDTO dto) {
        Customer existingCustomer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + dto.getCustomerId()));
        Customer updatedCustomer = CustomerMapper.INSTANCE.toEntity(dto);
        updatedCustomer.setCustomerId(existingCustomer.getCustomerId());
        customerRepository.save(updatedCustomer);
        return CustomerMapper.INSTANCE.toDTO(updatedCustomer);
    }

    public void deleteCustomer(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }

    public PaginationResponse<CustomerDTO> listPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Customer> customerPage = customerRepository.findAll(pageable);

        List<CustomerDTO> customerDTOList = customerPage.getContent().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());

        return PaginationResponse.<CustomerDTO>builder()
                .content(customerDTOList)
                .pageNo(customerPage.getNumber())
                .pageSize(customerPage.getSize())
                .totalElements(customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .isLast(customerPage.isLast())
                .build();
    }
}

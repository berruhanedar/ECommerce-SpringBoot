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


    public CustomerDTO getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return customerMapper.toDTO(customer);
    }

    public CustomerDTO createCustomer(NewCustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDTO(savedCustomer);
    }

    @Transactional
    public CustomerDTO updateCustomer(Integer id, UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        existingCustomer.setFirstName(updateCustomerRequestDTO.getFirstName());
        existingCustomer.setLastName(updateCustomerRequestDTO.getLastName());
        existingCustomer.setEmail(updateCustomerRequestDTO.getEmail());
        existingCustomer.setMobileNumber(updateCustomerRequestDTO.getMobileNumber());
        existingCustomer.setPassword(updateCustomerRequestDTO.getPassword());
        existingCustomer.setBalance(updateCustomerRequestDTO.getBalance());
        existingCustomer.setAddressId(updateCustomerRequestDTO.getAddressId());

        customerMapper.updateCustomerFromDTO(updateCustomerRequestDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toDTO(updatedCustomer);
    }

    public void deleteCustomer(Integer id) {
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










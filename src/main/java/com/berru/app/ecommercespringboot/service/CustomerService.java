package com.berru.app.ecommercespringboot.service;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.entity.Customer;
import com.berru.app.ecommercespringboot.exception.ResourceNotFoundException;
import com.berru.app.ecommercespringboot.mapper.CustomerMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import com.berru.app.ecommercespringboot.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;
    private final KafkaProducerService kafkaProducerService;

    @Transactional
    @Cacheable(value = "customers", key = "#id")
    public CustomerDTO getCustomerById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        return customerMapper.toDTO(customer);
    }

    @Transactional
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO createCustomer(NewCustomerRequestDTO dto) {
        Customer customer = customerMapper.toEntity(dto);

        Address address = addressRepository.findById(dto.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id " + dto.getAddressId()));

        address.setCustomer(customer);
        customer.getAddresses().add(address);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toDTO(savedCustomer);
    }

    @Transactional
    @CachePut(value = "customers", key = "#id")
    public CustomerDTO updateCustomer(Integer id, UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        customerMapper.updateCustomerFromDTO(updateCustomerRequestDTO, existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);

        return customerMapper.toDTO(updatedCustomer);
    }

    @Transactional
    @CacheEvict(value = "customers", key = "#id")
    public void deleteCustomer(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
        customerRepository.delete(customer);
    }

    @Transactional
    @Cacheable(value = "customersList", key = "#pageNo + '-' + #pageSize")
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

    @Transactional
    public List<CustomerDTO> searchCustomersByRsql(String query) {
        RSQLParser parser = new RSQLParser();
        Node rootNode = parser.parse(query);

        CustomRsqlVisitor<Customer> visitor = new CustomRsqlVisitor<>();
        Specification<Customer> spec = rootNode.accept(visitor);

        List<Customer> customers = customerRepository.findAll(spec);

        return customers.stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

}










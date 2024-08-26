package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.PaginationResponse;
import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    final CustomerService customerService;

    @GetMapping
    public ResponseEntity<PaginationResponse<CustomerDTO>> listPaginated(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginationResponse<CustomerDTO> paginationResponse = customerService.listPaginated(pageNo, pageSize);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody @Valid NewCustomerRequestDTO newCustomerRequestDTO) {
        CustomerDTO customerDTO = customerService.createCustomer(newCustomerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody @Valid UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, updateCustomerRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}








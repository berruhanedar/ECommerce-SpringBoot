package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO toDTO(Customer customer);

    Customer toEntity(NewCustomerRequestDTO dto);

    void updateCustomerFromDTO(UpdateCustomerRequestDTO updateCustomerRequestDTO, @MappingTarget Customer customer);

}
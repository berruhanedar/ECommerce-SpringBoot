package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.dto.NewCustomerRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateCustomerRequestDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO toDTO(Customer customer);

    // Yeni müşteri oluşturma için kullanılan DTO
    Customer toEntity(NewCustomerRequestDTO dto);

    // Mevcut müşteri güncellemesi için kullanılan DTO
    Customer toEntity(UpdateCustomerRequestDTO dto);
}

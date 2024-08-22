package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.CustomerDTO;
import com.berru.app.ecommercespringboot.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * rewuest ve response dto ları ayrı class olacak
     * @param customer
     * @return
     */
    @Mapping(source = "customerId", target = "id")
    CustomerDTO toDTO(Customer customer);
    @Mapping(source = "id", target = "customerId")
    Customer toEntity(CustomerDTO customerDTO);
}

package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface AddressMapper {


    AddressDTO toAddressDTO(Address address);

    Address toAddress(NewAddressRequestDTO newAddressRequestDTO);

    void updateAddressFromDTO(UpdateAddressRequestDTO updateAddressRequestDTO, @MappingTarget Address address);
}

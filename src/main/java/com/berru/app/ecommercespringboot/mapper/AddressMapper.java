package com.berru.app.ecommercespringboot.mapper;


import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDTO toAddressDTO(Address address);

    Address toAddress(AddressDTO addressDTO);

    Address toAddress(NewAddressRequestDTO newAddressRequestDTO);

    AddressDTO toAddressDTO(NewAddressRequestDTO newAddressRequestDTO);

    Address toAddress(UpdateAddressRequestDTO updateAddressRequestDTO);

    AddressDTO toAddressDTO(UpdateAddressRequestDTO updateAddressRequestDTO);
}

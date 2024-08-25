package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.mapper.AddressMapper;
import com.berru.app.ecommercespringboot.mapper.CustomerMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public AddressDTO createAddress(NewAddressRequestDTO newAddressRequestDTO) {
        Address address = AddressMapper.INSTANCE.toAddress(newAddressRequestDTO);
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.INSTANCE.toAddressDTO(savedAddress);
    }

    public AddressDTO getAddressById(Integer id) {
        Optional<Address> address = addressRepository.findById(id);
        return address.map(AddressMapper.INSTANCE::toAddressDTO).orElse(null);
    }

    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(AddressMapper.INSTANCE::toAddressDTO)
                .toList();
    }

    // mapper kullanılabilir
    @Transactional
    public AddressDTO updateAddress(Integer id, UpdateAddressRequestDTO updateAddressRequestDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        // Update fields
        address.setAddressName(updateAddressRequestDTO.getAddressName());
        address.setStreet(updateAddressRequestDTO.getStreet());
        address.setBuildingName(updateAddressRequestDTO.getBuildingName());
        address.setCity(updateAddressRequestDTO.getCity());
        address.setCountry(updateAddressRequestDTO.getCountry());
        Address updatedAddress = addressRepository.save(address);
        return AddressMapper.INSTANCE.toAddressDTO(updatedAddress);
    }


    @Transactional
    public void deleteAddress(Integer id) {
        addressRepository.deleteById(id);
    }
}

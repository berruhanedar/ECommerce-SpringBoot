package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.mapper.AddressMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    @Transactional
    public AddressDTO createAddress(NewAddressRequestDTO newAddressRequestDTO) {
        Address address = addressMapper.toAddress(newAddressRequestDTO);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toAddressDTO(savedAddress);
    }

    @Transactional
    @Cacheable(value = "addresses", key = "#id")
    public AddressDTO getAddressById(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return addressMapper.toAddressDTO(address);
    }

    @Transactional
    @Cacheable(value = "allAddresses")
    public List<AddressDTO> getAllAddresses() {
        return addressRepository.findAll().stream()
                .map(addressMapper::toAddressDTO)
                .toList();
    }

    @Transactional
    @CachePut(value = "addresses", key = "#id")
    public AddressDTO updateAddress(Integer id, UpdateAddressRequestDTO updateAddressRequestDTO) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
        addressMapper.updateAddressFromDTO(updateAddressRequestDTO, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.toAddressDTO(updatedAddress);
    }

    @Transactional
    @CacheEvict(value = "addresses", key = "#id")
    public void deleteAddress(Integer id) {
        addressRepository.deleteById(id);
    }
}

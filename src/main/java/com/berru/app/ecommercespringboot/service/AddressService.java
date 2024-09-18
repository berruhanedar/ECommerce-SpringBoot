package com.berru.app.ecommercespringboot.service;

import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.entity.Address;
import com.berru.app.ecommercespringboot.mapper.AddressMapper;
import com.berru.app.ecommercespringboot.repository.AddressRepository;
import com.berru.app.ecommercespringboot.rsql.CustomRsqlVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final KafkaProducerService kafkaProducerService;

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

    @Transactional
    public List<AddressDTO> searchAddressesByRsql(String query) {
        RSQLParser parser = new RSQLParser();
        Node rootNode = parser.parse(query);

        CustomRsqlVisitor<Address> visitor = new CustomRsqlVisitor<>();
        Specification<Address> spec = rootNode.accept(visitor);

        List<Address> addresses = addressRepository.findAll(spec);

        return addresses.stream()
                .map(addressMapper::toAddressDTO)
                .collect(Collectors.toList());
    }
}

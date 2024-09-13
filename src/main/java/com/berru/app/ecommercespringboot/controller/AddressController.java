package com.berru.app.ecommercespringboot.controller;


import com.berru.app.ecommercespringboot.dto.AddressDTO;
import com.berru.app.ecommercespringboot.dto.NewAddressRequestDTO;
import com.berru.app.ecommercespringboot.dto.UpdateAddressRequestDTO;
import com.berru.app.ecommercespringboot.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/addresses")
public class AddressController {
    final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody NewAddressRequestDTO newAddressRequestDTO) {
        AddressDTO addressDTO = addressService.createAddress(newAddressRequestDTO);
        return ResponseEntity.ok(addressDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Integer id) {
        AddressDTO addressDTO = addressService.getAddressById(id);
        return addressDTO != null ? ResponseEntity.ok(addressDTO) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        List<AddressDTO> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Integer id, @RequestBody UpdateAddressRequestDTO updateAddressRequestDTO) {
        AddressDTO addressDTO = addressService.updateAddress(id, updateAddressRequestDTO);
        return ResponseEntity.ok(addressDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<AddressDTO>> searchAddressesByRsql(@RequestParam String query) {
        List<AddressDTO> addresses = addressService.searchAddressesByRsql(query);
        return ResponseEntity.ok(addresses);
    }

}

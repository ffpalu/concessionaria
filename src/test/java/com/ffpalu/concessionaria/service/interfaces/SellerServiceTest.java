package com.ffpalu.concessionaria.service.interfaces;

import com.ffpalu.concessionaria.dto.request.SellerDetailsRequest;
import com.ffpalu.concessionaria.entity.Credential;
import com.ffpalu.concessionaria.entity.Seller;
import com.ffpalu.concessionaria.entity.User;
import com.ffpalu.concessionaria.exceptions.SellerException;
import com.ffpalu.concessionaria.repository.SellerRepository;
import com.ffpalu.concessionaria.service.SellerServiceImpl;
import com.ffpalu.concessionaria.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @Test
    void checkIfPresentShouldReturnTrueWhenFound() {
        String employeeCode = "12345";
        when(sellerRepository.existsByEmployeeCode(employeeCode)).thenReturn(true);
        assertTrue(sellerService.checkIfPresent(employeeCode));
    }

    @Test
    void checkIfPresentShouldReturnFalseWhenNotFound() {
        String employeeCode = "12345";
        when(sellerRepository.existsByEmployeeCode(employeeCode)).thenReturn(false);
        assertFalse(sellerService.checkIfPresent(employeeCode));
    }

    @Test
    void createSellerShouldSaveWhenSellerNotExists() {

        SellerDetailsRequest sellerDetailsRequest = new SellerDetailsRequest();
        User user = new User();

        String employeeCode = "12345";
        UUID id =  UUID.randomUUID();
        sellerDetailsRequest.setEmployeeCode(employeeCode);
        Seller seller = Seller.builder().employeeCode(employeeCode).build();
        Seller sellerSaved = Seller.builder().id(id).employeeCode(employeeCode).build();


        when(sellerRepository.existsByEmployeeCode(employeeCode)).thenReturn(false);
        when(mapper.mapToSeller(sellerDetailsRequest,  user)).thenReturn(seller);
        when(sellerRepository.save(seller)).thenReturn(sellerSaved);

        Seller result = sellerService.createSeller(sellerDetailsRequest, user);

        assertEquals(employeeCode, result.getEmployeeCode());
        assertEquals(id, result.getId());
        verify(sellerRepository).save(any());

    }

    @Test
    void createSellerShouldThrowWhenSellerExists() {

        SellerDetailsRequest sellerDetailsRequest = new SellerDetailsRequest();
        User user = new User();
        String employeeCode = "12345";
        sellerDetailsRequest.setEmployeeCode(employeeCode);

        when( sellerRepository.existsByEmployeeCode(employeeCode)).thenReturn(true);

        assertThrows(SellerException.class, () -> sellerService.createSeller(sellerDetailsRequest, user));
        verify(sellerRepository, never()).save(any());
    }

    @Test
    void getSellerByOrderedByNumSalesShouldReturnPageOfSellerOrderedByNum() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("salesCount").descending());

        Seller seller1 = Seller.builder().employeeCode("12345").salesCount(5).build();
        Seller seller2 = Seller.builder().employeeCode("12345").salesCount(10).build();

        Page<Seller> sellers = new PageImpl<>(List.of(seller2,seller1), pageable, 10);

        when( sellerRepository.findAll(pageable) ).thenReturn(sellers);

        assertEquals(sellers, sellerService.getSellerByOrderedByNumSales(pageable));
    }

    @Test
    void findByUsernameShouldReturnOptionalWhenFound() {
        String employeeCode = "12345";
        String username = "username";
        Credential credential = Credential.builder().username(username).build();
        User user = User.builder().credential(credential).build();
        Seller seller = Seller.builder().employeeCode(employeeCode).user(user).build();

        when(sellerRepository.findByUsername(username)).thenReturn(Optional.of(seller));

        Optional<Seller> result = sellerService.findByUsername(username);


        assertEquals(Optional.of(seller), result);
        assertTrue(result.isPresent());
        assertEquals(seller, result.get());
        assertEquals(seller.getUser().getCredential().getUsername(), result.get().getUser().getCredential().getUsername());
    }

    @Test
    void findById() {
        UUID id = UUID.randomUUID();
        Seller seller = Seller.builder().id(id).build();

        when( sellerRepository.findById(id) ).thenReturn(Optional.of(seller));
        Optional<Seller> result = sellerService.findById(id);


        assertEquals(Optional.of(seller), result);
        assertTrue(result.isPresent());
        assertEquals(seller.getId(), result.get().getId());
    }
}
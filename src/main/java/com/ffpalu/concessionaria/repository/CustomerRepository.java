package com.ffpalu.concessionaria.repository;

import com.ffpalu.concessionaria.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findById(UUID id);
    
    boolean existsByCF(String cf);

    Optional<Customer> findByCF(String CF);

    Page<Customer> findByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

}

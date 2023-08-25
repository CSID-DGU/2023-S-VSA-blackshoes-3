package com.travelvcommerce.uploadservice.repository;

import com.travelvcommerce.uploadservice.entity.Uploader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UploaderRepository extends JpaRepository<Uploader, Long> {
    boolean existsBySellerId(String sellerId);

    Optional<Uploader> findBySellerId(String sellerId);
}

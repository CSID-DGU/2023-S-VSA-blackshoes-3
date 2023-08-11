package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface SubscribedTagRepository extends JpaRepository<SubscribedTag, String> {
    List<SubscribedTag> findByUserId(String userId);
    void deleteByUserIdAndTagId(String userId, String tagId);
    boolean existsByUserId(String userId);
    boolean existsByUserIdAndTagId(String userId, String tagId);

    void deleteByUserId(String userId);
}

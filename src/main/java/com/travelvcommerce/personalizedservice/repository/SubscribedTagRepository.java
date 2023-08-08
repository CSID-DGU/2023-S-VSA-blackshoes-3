package com.travelvcommerce.personalizedservice.repository;

import com.travelvcommerce.personalizedservice.entity.SubscribedTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscribedTagRepository extends JpaRepository<SubscribedTag, String> {
    public List<SubscribedTag> findByUserId(String userId);
    public void deleteByUserIdAndTagId(String userId, String tagId);
    public boolean existsByUserId(String userId);
    public boolean existsByUserIdAndTagId(String userId, String tagId);

}

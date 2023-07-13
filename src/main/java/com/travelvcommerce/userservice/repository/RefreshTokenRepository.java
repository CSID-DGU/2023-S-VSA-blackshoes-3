package com.travelvcommerce.userservice.repository;
import org.springframework.data.repository.CrudRepository;
import com.travelvcommerce.userservice.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

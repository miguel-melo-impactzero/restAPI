//Repository interface for the User mySQL table

package com.impactzero.restAPI.Repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.impactzero.restAPI.Classes.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Implementation of CRUD operations for the User Table
@Repository
public interface UserRepository extends CrudRepository<User, Long>{

    //Converter Query para HQL
    //Má prática usar queries Hard-coded
    @Query(value = "Select * from User where username LIKE :username or email LIKE :email",nativeQuery = true)
    List<User> checkConstrains(@Param("username") String username,
                               @Param("email") String email
    );

    public User findByName(String name);

}


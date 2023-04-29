package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);

    //get user based on userName

    User findByUserNameAndIsDeleted(String username,Boolean deleted);

    @Transactional
    void deleteByUserName(String username);
    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean deleted);
}

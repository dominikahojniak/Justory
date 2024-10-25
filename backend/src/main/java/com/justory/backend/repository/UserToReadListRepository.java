package com.justory.backend.repository;
import com.justory.backend.api.internal.UserToReadList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserToReadListRepository extends JpaRepository<UserToReadList, Integer> {

    List<UserToReadList> findByUserId(Integer userId);
    void deleteByUserIdAndBookId(Integer userId, Integer bookId);
    Optional<UserToReadList> findByUserIdAndBookId(Integer userId, Integer bookId);
}
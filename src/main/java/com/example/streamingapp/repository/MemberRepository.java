package com.example.streamingapp.repository;

import com.example.streamingapp.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByMemberId(String id);
    Optional<Member> findByName(String name);
    Optional<Member> findByHandle(String handle);
    @Query(value = "select salt from member where member_id = :id", nativeQuery = true)
    String getSalt(@Param("id") String id);


}

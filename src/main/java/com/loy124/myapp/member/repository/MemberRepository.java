package com.loy124.myapp.member.repository;

import com.loy124.myapp.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(@Param("email") String email);


}

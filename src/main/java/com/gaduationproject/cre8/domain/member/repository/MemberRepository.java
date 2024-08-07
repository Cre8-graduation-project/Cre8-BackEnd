package com.gaduationproject.cre8.domain.member.repository;

import com.gaduationproject.cre8.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByNickName(String nickName);

    Boolean existsByLoginId(String loginId);

    Optional<Member> findMemberByLoginId(String loginId);





}

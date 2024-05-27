package com.gaduationproject.cre8.member.repository;

import com.gaduationproject.cre8.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Boolean existsByEmail(String email);
    Boolean existsByNickName(String nickName);

}

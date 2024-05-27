package com.gaduationproject.cre8.member.repository;

import com.gaduationproject.cre8.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

}

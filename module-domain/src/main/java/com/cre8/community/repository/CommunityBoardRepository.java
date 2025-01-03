package com.cre8.community.repository;


import com.cre8.community.entity.CommunityBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityBoardRepository extends JpaRepository<CommunityBoard,Long> {

}

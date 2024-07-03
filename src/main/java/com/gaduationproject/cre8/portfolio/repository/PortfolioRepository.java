package com.gaduationproject.cre8.portfolio.repository;

import com.gaduationproject.cre8.portfolio.entity.Portfolio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    @Query("select p from Portfolio p left outer join fetch p.portfolioImageList where p.member.id=:memberId")
    List<Portfolio> findByMemberIdWithFetchPortfolioImage(@Param("memberId") Long memberId);

    @Query("select p from Portfolio p left outer join fetch p.portfolioImageList left outer join fetch p.workFieldTag where p.id=:portfolioId")
    Optional<Portfolio> findByPortfolioIdWithFetchImageAndWorkFieldTag(@Param("portfolioId") Long portfolioId);

}
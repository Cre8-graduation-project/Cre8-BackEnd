package com.gaduationproject.cre8.domain.portfolio.repository;

import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioWorkFieldChildTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioWorkFieldChildTagRepository extends JpaRepository<PortfolioWorkFieldChildTag,Long> {

    void deleteByPortfolio(Portfolio portfolio);


    @Query("select pwfct from PortfolioWorkFieldChildTag pwfct join fetch pwfct.workFieldChildTag where pwfct.portfolio.id=:portfolioId")
    List<PortfolioWorkFieldChildTag> findByPortfolioIdWithFetchWorkFieldChildTag(@Param("portfolioId") Long portfolioId);
}

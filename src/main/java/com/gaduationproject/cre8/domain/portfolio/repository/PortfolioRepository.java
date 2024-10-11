package com.gaduationproject.cre8.domain.portfolio.repository;

import com.gaduationproject.cre8.domain.member.entity.Member;
import com.gaduationproject.cre8.domain.portfolio.entity.Portfolio;
import java.util.List;
import java.util.Optional;
import javax.sound.sampled.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {

    @Query("select p from Portfolio p left outer join fetch p.portfolioImageList pil where p.member.id=:memberId order by p.id,pil.id")
    List<Portfolio> findByMemberIdWithFetchPortfolioImage(@Param("memberId") Long memberId);

    @Query("select p from Portfolio p left outer join fetch p.portfolioImageList left outer join fetch p.workFieldTag where p.id=:portfolioId")
    Optional<Portfolio> findByPortfolioIdWithFetchImageAndWorkFieldTag(@Param("portfolioId") Long portfolioId);

    List<Portfolio> findByMember(final Member member);

    void deleteByMember(final Member member);


}

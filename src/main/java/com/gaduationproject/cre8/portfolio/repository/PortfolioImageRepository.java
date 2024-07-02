package com.gaduationproject.cre8.portfolio.repository;

import com.gaduationproject.cre8.portfolio.entity.Portfolio;
import com.gaduationproject.cre8.portfolio.entity.PortfolioImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage,Long> {

    List<PortfolioImage> findByPortfolio(Portfolio portfolio);

}

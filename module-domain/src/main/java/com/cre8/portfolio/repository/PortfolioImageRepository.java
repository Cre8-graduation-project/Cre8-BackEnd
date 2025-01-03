package com.cre8.portfolio.repository;


import com.cre8.portfolio.entity.Portfolio;
import com.cre8.portfolio.entity.PortfolioImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioImageRepository extends JpaRepository<PortfolioImage,Long> {

    List<PortfolioImage> findByPortfolio(Portfolio portfolio);

    void deleteByPortfolio(final Portfolio portfolio);



}

package com.cre8.elasticsearch.repository;

import com.cre8.elasticsearch.document.PortfolioImageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioImageDocumentRepository extends ElasticsearchRepository<PortfolioImageDocument,String> {

    void deleteByPortfolioImageId(final Long portfolioImageId);
}

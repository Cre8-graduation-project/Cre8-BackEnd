package com.gaduationproject.cre8.externalApi.elasticsearch.repository;

import com.gaduationproject.cre8.externalApi.elasticsearch.document.PortfolioImageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioImageDocumentRepository extends ElasticsearchRepository<PortfolioImageDocument,String> {

    void deleteByPortfolioImageId(final Long portfolioImageId);
}

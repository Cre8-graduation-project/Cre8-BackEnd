package com.gaduationproject.cre8.externalApi.elasticsearch.service;

import com.gaduationproject.cre8.domain.portfolio.entity.PortfolioImage;
import com.gaduationproject.cre8.externalApi.elasticsearch.document.PortfolioImageDocument;
import com.gaduationproject.cre8.externalApi.elasticsearch.repository.PortfolioImageDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PortfolioImageDocumentService {

    private final PortfolioImageDocumentRepository portfolioImageDocumentRepository;

    public void delete(Long portfolioImageId){
        portfolioImageDocumentRepository.deleteByPortfolioImageId(portfolioImageId);
    }

}

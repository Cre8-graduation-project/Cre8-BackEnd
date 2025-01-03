package com.cre8.elasticsearch.service;


import com.cre8.elasticsearch.repository.PortfolioImageDocumentRepository;
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

package com.cre8.elasticsearch.document;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "portfolio")
public class PortfolioImageDocument {

    @Id
    private String id;

    private Long portfolioId;

    private String accessUrl;

    private Long portfolioImageId;

    private List<Float> vector;

}

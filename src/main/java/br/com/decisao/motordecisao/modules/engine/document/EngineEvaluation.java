package br.com.decisao.motordecisao.modules.engine.document;

import br.com.decisao.motordecisao.modules.data.dto.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "engine-evaluation")
public class EngineEvaluation {

    private static final Long ZERO = 0L;

    @Id
    private String id;

    private String engineId;

    private List<EngineProduct> products;

    private List<EngineProduct> disapprovedProducts;

    private Person person;

    private List<ConsultedApiService> consultedApis;

    private ApiData apiData;

    private LocalDateTime evaluationStart;

    private LocalDateTime evaluationFinish;

    private Long totalTime;

    public static EngineEvaluation convertFrom(PayloadRequest payloadRequest,
                                               LocalDateTime evaluationStart) {
        var finish = LocalDateTime.now();
        return EngineEvaluation
            .builder()
            .engineId(UUID.randomUUID().toString())
            .products(payloadRequest.getProdutos())
            .disapprovedProducts(payloadRequest.getProdutosReprovados())
            .person(payloadRequest.getPessoa())
            .consultedApis(payloadRequest.getApisConsultadas())
            .apiData(payloadRequest.getDadosApis())
            .evaluationStart(evaluationStart)
            .evaluationFinish(finish)
            .totalTime(getTotalTime(Duration.between(evaluationStart, finish)))
            .build();
    }

    private static Long getTotalTime(Duration tempoTotal) {
        if (tempoTotal.getSeconds() <= ZERO) {
            return TimeUnit.MILLISECONDS.convert(tempoTotal.getNano(), TimeUnit.NANOSECONDS);
        }
        return tempoTotal.getSeconds();
    }
}

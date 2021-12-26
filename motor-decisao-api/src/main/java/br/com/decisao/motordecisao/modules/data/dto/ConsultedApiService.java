package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultedApiService {

    private Api id;

    @JsonProperty("success")
    private boolean sucesso;

    private Integer status;

    @JsonProperty("reason")
    private String razao;

    public static ConsultedApiService getConsultedApiService(Api apiService,
                                                             List<ConsultedApiService> consultedApiServices) {
        return !ObjectUtils.isEmpty(consultedApiServices)
            && !ObjectUtils.isEmpty(apiService)
            ? consultedApiServices
            .stream()
            .filter(api -> apiService.equals(api.id))
            .findFirst()
            .orElse(null)
        : null;
    }

    public static ConsultedApiService create(Api api,
                                             boolean success,
                                             int status,
                                             String reason) {
        return ConsultedApiService
            .builder()
            .id(api)
            .status(status)
            .sucesso(success)
            .razao(reason)
            .build();
    }
}

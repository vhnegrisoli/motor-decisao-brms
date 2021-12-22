package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.enums.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiWrapperData {

    private Api serviceId;
    private PayloadRequest payload;
}


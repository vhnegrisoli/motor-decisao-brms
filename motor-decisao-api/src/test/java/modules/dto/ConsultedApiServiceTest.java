package modules.dto;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static modules.mocks.DtoMocks.getOneConsultedApiService;
import static org.assertj.core.api.Assertions.assertThat;

public class ConsultedApiServiceTest {

    @Test
    @DisplayName("Should return a consulted api when informing an existing api ID")
    public void shouldReturnConsultedApiWhenInformingExistingApiId() {
        var givenApiServiceList = Collections.singletonList(getOneConsultedApiService());

        var expectedApiService = getOneConsultedApiService();

        assertThat(ConsultedApiService.getConsultedApiService(Api.CPF_VALIDO,givenApiServiceList))
            .usingRecursiveComparison()
            .isEqualTo(expectedApiService);
    }

    @Test
    @DisplayName("Should return null when informing a not existing api ID")
    public void shouldReturnNullWhenInformingNotExistingApiId() {
        var givenApiServiceList = Collections.singletonList(getOneConsultedApiService());

        assertThat(ConsultedApiService.getConsultedApiService(Api.CEP_VALIDO, givenApiServiceList)).isNull();
    }

    @Test
    @DisplayName("Should return null when informing a null api ID")
    public void shouldReturnNullWhenInformingNullApiId() {
        var givenApiServiceList = Collections.singletonList(getOneConsultedApiService());

        assertThat(ConsultedApiService.getConsultedApiService(null, givenApiServiceList)).isNull();
    }

    @Test
    @DisplayName("Should return null when informing a null consulted API service list")
    public void shouldReturnNullWhenInformingNullApiServiceList() {
        assertThat(ConsultedApiService.getConsultedApiService(Api.CPF_VALIDO, null)).isNull();
    }

    @Test
    @DisplayName("Should return null when informing a null")
    public void shouldReturnNullWhenInformingNull() {
        assertThat(ConsultedApiService.getConsultedApiService(null, null)).isNull();
    }

    @Test
    @DisplayName("Should create a ConsultedApiService when informing data")
    public void shouldCreateAConsultedApiServiceWhenInformingData() {
        var expectedApiService = getOneConsultedApiService();

        assertThat(ConsultedApiService.create(Api.CPF_VALIDO, true, 200, null))
            .usingRecursiveComparison()
            .isEqualTo(expectedApiService);
    }
}

package br.com.decisao.logapi.log.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "log")
public class Log {

    @Id
    private String id;

    private String transactionId;

    private String serviceId;

    private String message;

    private String logLevel;

    private String serviceName;
}

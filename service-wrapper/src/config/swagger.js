export const SWAGGER_DOCS = {
    openapi: "3.0.1",
    info: {
        title: "Service Wrapper",
        description: "API que serve como um wrapper para consulta dos serviços",
        version: "1.0"
    },
    host: "localhost:3000",
    basePath: "/",
    schemes: [
        "http"
    ],
    tags: [{
        name: "Requisição Wrapper",
    }],
    paths: {
        "/api/wrapper": {
            post: {
                parameters: [{
                    in: 'header',
                    name: 'transactionId',
                    schema: {
                        type: 'string'
                    },
                    required: true,
                    description: "TransactionID para tracking"
                }],
                tags: ["Requisição Wrapper"],
                description: "Requisição ao Wrapper informando os dados do serviço a ser consultado.",
                operationId: "WRAPPER",
                requestBody: {
                    content: {
                        "application/json": {
                            schema: {
                                $ref: "#components/schemas/WrapperData"
                            }
                        }
                    },
                    required: true,
                },
                responses: {
                    "200": {
                        description: "Wrapper executado com sucesso.",
                        content: {
                            "application/json": {
                                schema: {
                                    $ref: "#/components/schemas/Payload",
                                },
                            },
                        },
                    },
                    "400": {
                        description: "Erro ao executar wrapper",
                        content: {
                            "application/json": {
                                schema: {
                                    $ref: "#/components/schemas/Error",
                                },
                            },
                        },
                    },
                },
            }
        }
    },
    components: {
        schemas: {
            WrapperData: {
                type: "object",
                properties: {
                    serviceId: {
                        type: "string"
                    },
                    payload: {
                        type: "object",
                        properties: {
                            person: {
                                type: "object",
                                properties: {
                                    document: {
                                        type: "string"
                                    },
                                    birthday: {
                                        type: "string"
                                    },
                                    postcode: {
                                        type: "string"
                                    },
                                }
                            },
                            consultedApis: {
                                type: "array",
                                items: {
                                    type: "object",
                                    properties: {
                                        id: {
                                            type: "string"
                                        },
                                        code: {
                                            type: "integer"
                                        },
                                        success: {
                                            type: "boolean"
                                        },
                                        reason: {
                                            type: "string"
                                        },
                                    }
                                }
                            },
                            apiData: {
                                type: "object",
                                properties: {
                                    validCpf: {
                                        type: "object",
                                        properties: {
                                            cpf: {
                                                type: "string"
                                            },
                                            validCpf: {
                                                type: "boolean"
                                            }
                                        }
                                    },
                                    cleanCpf: {
                                        type: "object",
                                        properties: {
                                            cpf: {
                                                type: "string"
                                            },
                                            cleanCpf: {
                                                type: "boolean"
                                            }
                                        }
                                    },
                                    birthday: {
                                        type: "object",
                                        properties: {
                                            age: {
                                                type: "integer"
                                            }
                                        }
                                    },
                                    cep: {
                                        type: "object",
                                        properties: {
                                            cep: {
                                                type: "string"
                                            },
                                            validCep: {
                                                type: "boolean"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            Payload: {
                type: "object",
                properties: {
                    person: {
                        type: "object",
                        properties: {
                            document: {
                                type: "string"
                            },
                            birthday: {
                                type: "string"
                            },
                            postcode: {
                                type: "string"
                            },
                        }
                    },
                    consultedApis: {
                        type: "array",
                        items: {
                            type: "object",
                            properties: {
                                id: {
                                    type: "string"
                                },
                                code: {
                                    type: "integer"
                                },
                                success: {
                                    type: "boolean"
                                },
                                reason: {
                                    type: "string"
                                },
                            }
                        }
                    },
                    apiData: {
                        type: "object",
                        properties: {
                            validCpf: {
                                type: "object",
                                properties: {
                                    cpf: {
                                        type: "string"
                                    },
                                    validCpf: {
                                        type: "boolean"
                                    }
                                }
                            },
                            cleanCpf: {
                                type: "object",
                                properties: {
                                    cpf: {
                                        type: "string"
                                    },
                                    cleanCpf: {
                                        type: "boolean"
                                    }
                                }
                            },
                            birthday: {
                                type: "object",
                                properties: {
                                    age: {
                                        type: "integer"
                                    }
                                }
                            },
                            cep: {
                                type: "object",
                                properties: {
                                    cep: {
                                        type: "string"
                                    },
                                    validCep: {
                                        type: "boolean"
                                    }
                                }
                            }
                        }
                    }
                }
            },
            Error: {
                type: "object",
                properties: {
                    status: {
                        type: "number",
                    },
                    message: {
                        type: "string",
                    },
                },
            }
        }
    }
};
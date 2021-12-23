const env = process.env.NODE_ENV;

console.log(env)

export const CPF_API_URL = env === 'container' ? 'http://cpf-limpo-api:8081' : 'http://localhost:8081';
export const CEP_VALIDO_API_URL = env === 'container' ? 'http://cep-valido-api:8082' : 'http://localhost:8082';
export const DATA_VALIDA_API_URL = env === 'container' ? 'http://data-valida-api:8083' : 'http://localhost:8083';
const env = process.env.NODE_ENV;

console.log(env)

export const DATA_VALIDA_API_URL = env === 'container' ? 'http://data-valida-api:8083' : 'http://localhost:8083'
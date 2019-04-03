import { Configuration } from '../api';

/**
 * A singleton instance of the configuration object.
 *
 * getConfiguration is what will be used by all the api in the generated code to make the server
 * calls. This module can be used by the application to set specific credentials or properties of
 * the configuration. The set properties will be used by the generated code in subsequent requests.
 * Ex: to set the jwt token on login, which will be used by the generated code to pass it in as
 * header 'Authorization' in all subsequent requests.
 */

/** the configuration singleton instance  */
let config = new Configuration({});

/** set the jwt token for authorization */
export function setJWTToken(jwtToken: string) {
    config.apiKey = jwtToken;
}

/** set the username, password for basic authentication */
export function setBasicAuth(username: string, password: string) {
    config.username = username;
    config.password = password;
}

/** set the oauth2 authentication */
export function setOAuth2(accessToken: string | ((name: string, scopes?: string[]) => string)) {
    config.accessToken = accessToken;
}

/** set the base path for the server calls */
export function setBasePath(basePath: string) {
    config.basePath = basePath;
}

/**
 * get the configuration based on the current set of configuration parameters
 *
 * NOTE: to be used only by the generated code, do not modify the returned configuration.
 */
export function getConfiguration(): Configuration {
    return config;
}

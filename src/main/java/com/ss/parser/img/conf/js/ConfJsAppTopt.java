/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ss.parser.img.conf.js;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ss.config.js.ExceptConf;
import com.fasterxml.jackson.databind.JsonNode;
import com.ss.config.js.ConfJsApp;
import com.ss.config.js.ConfJsDb;

/**
 * @author vlitenko
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ConfJsAppTopt extends ConfJsApp {

    private String nameServer;
    private String urlBase;
    private String serverType;
    public String actionServiceUrlCreate;
    public String actionServiceUrlClose;
    public String keycloakOpenidUrl;
    public String keycloakPublicKey;
    @JsonIgnore
    public String keycloakClientSecret;
    public String keycloakClientUrl;
    public String domain;
    private int hikariPoolMaxSize;

    private int errorsSize;
    private int errorsInterval;
    private int errorsSleepInterval;


    public static final String SERVER_TYPE_DEV = "dev";
    public static final String SERVER_TYPE_TEST = "test";
    public static final String SERVER_TYPE_PREPROD = "preprod";
    public static final String SERVER_TYPE_PROD = "prod";

    public ConfJsAppTopt() {
        super(ConfJsDb.knownDb);
    }

    public ConfJsAppTopt(ConfJsApp p_kCopy) {
        super(p_kCopy);
    }

    @Override
    protected void initApp(JsonNode p_xParser) throws ExceptConf {
        try {

            // TECHNICAL
            nameServer = getStringRequired(p_xParser, "name");
            urlBase = getStringRequired(p_xParser, "url_base");
            serverType = getStringRequired(p_xParser, "server_type");
            domain = getStringRequired(p_xParser, "domain");
            hikariPoolMaxSize = getIntRequired(p_xParser, "hikari_pool_max_size");
            // API
            actionServiceUrlCreate = getStringRequired(p_xParser, "action_service_url_create");
            actionServiceUrlClose = getStringRequired(p_xParser, "action_service_url_close");
            //ERRORS
            errorsSize = getIntRequired(p_xParser, "attack_errors_size");
            errorsInterval = getIntRequired(p_xParser, "attack_errors_interval_sec");
            errorsSleepInterval = getIntRequired(p_xParser, "attack_errors_sleep_interval_sec");
            //keycloak
            keycloakOpenidUrl =  getStringRequired(p_xParser, "keycloak_openid_url");
            keycloakPublicKey =  getStringRequired(p_xParser, "keycloak_public_key");
            keycloakClientSecret =  getStringRequired(p_xParser, "keycloak_client_secret");
            keycloakClientUrl =  getStringRequired(p_xParser, "keycloak_client_url");

        } catch (RuntimeException ex) {
            throw new ExceptConf("ErrConfA1", "Can't process project configuration",
                    ex.getMessage(), ex);
        }
    }

    public String getNameServer() {
        return nameServer;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public String getActionServiceUrlCreate() {
        return actionServiceUrlCreate;
    }

    public String getActionServiceUrlClose() {
        return actionServiceUrlClose;
    }

    public String getServerType() {
        return serverType;
    }
    public String getDomain() {
        return domain;
    }

    public int getErrorsSize() {
        return errorsSize;
    }

    public int getErrorsInterval() {
        return errorsInterval;
    }

    public int getErrorsSleepInterval() {
        return errorsSleepInterval;
    }

    public String getKeycloakOpenidUrl() {
        return keycloakOpenidUrl;
    }

    public String getKeycloakPublicKey() {
        return keycloakPublicKey;
    }

    public String getKeycloakClientSecret() {
        return keycloakClientSecret;
    }

    public String getKeycloakClientUrl() {
        return keycloakClientUrl;
    }

    public int getHikariPoolMaxSize() {
        return hikariPoolMaxSize;
    }

    @Override
    public String toString() {
        return "urlBase=" + urlBase + "\n"
                + "serverType=" + serverType + "\n"
                + "domain=" + domain + "\n"
                + "actionServiceUrlCreate=" + actionServiceUrlCreate + "\n"
                + "actionServiceUrlClose=" + actionServiceUrlClose + "\n"
                + "keycloakOpenidUrl=" + keycloakOpenidUrl + "\n"
                + "keycloakPublicKey=" + keycloakPublicKey + "\n"
                + "keycloakClientUrl=" + keycloakClientUrl + "\n";

    }
}

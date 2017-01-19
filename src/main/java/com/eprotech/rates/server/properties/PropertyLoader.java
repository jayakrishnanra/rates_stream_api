package com.eprotech.rates.server.properties;

import java.io.*;
import java.util.Properties;

/**
 * Created by mercury on 18-Aug-16.
 */
public class PropertyLoader {
    private static final String PROVIDER_PROPERTIES = "provider.properties";
    private static final PropertyLoader ourInstance = new PropertyLoader();

    private static final String PROVIDER_DOMAIN_PROP_NAME = "provider.domain";
    private static final String PROVIDER_ACCESS_TOKEN_PROP_NAME = "provider.access_token";
    private static final String PROVIDER_ACCOUNT_ID_PROP_NAME = "provider.account_id";

    private final String domainUrl;
    private final String access_token;
    private final String account_id;

    private Properties PROPERTIES;

    private PropertyLoader() {
        PROPERTIES = new Properties();
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            System.out.println(contextClassLoader);
            PROPERTIES.load(contextClassLoader.getResourceAsStream(PROVIDER_PROPERTIES));
        } catch (IOException e) {
            e.printStackTrace();
        }

        domainUrl = PROPERTIES.getProperty(PROVIDER_DOMAIN_PROP_NAME);
        access_token = PROPERTIES.getProperty(PROVIDER_ACCESS_TOKEN_PROP_NAME);
        account_id = PROPERTIES.getProperty(PROVIDER_ACCOUNT_ID_PROP_NAME);
    }

    public static PropertyLoader getInstance() {
        System.out.println("getInstance");
        return ourInstance;
    }

    public String getDomainUrl() {
        return domainUrl;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getAccount_id() {
        return account_id;
    }
}

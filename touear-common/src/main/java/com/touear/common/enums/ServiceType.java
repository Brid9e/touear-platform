package com.touear.common.enums;

import lombok.Getter;

@Getter
public enum ServiceType {

    APP_MANAGE("manage","public", "apk,ipa",100 * 1024 * 1024),
    ;

    private final String serviceType;
    private final String bucket;
    private final String extension;
    private final long size;


    ServiceType(String serviceType, String bucket, String extension, long size) {
        this.serviceType = serviceType;
        this.bucket = bucket;
        this.extension = extension;
        this.size = size;
    }

    public static boolean isValidExtension(String serviceType) {
        for (ServiceType type : ServiceType.values()) {
            if (type.getServiceType().equals(serviceType)) {
                return true;
            }
        }
        return false;
    }

    public static ServiceType getByServiceType(String serviceType) {
        for (ServiceType type : ServiceType.values()) {
            if (type.getServiceType().equals(serviceType)) {
                return type;
            }
        }
        return null;
    }
}

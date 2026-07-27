package com.example.eco_service.dto.main_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Плоская строка публичного реестра (waste × factory) для portal. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortalWasteRowDto {
    private String wasteCode;
    private String wasteName;
    private String registrationNumber;
    private String objectName;
    private String objectLocation;
    private String objectPhone;
    private String ownerName;
    private String ownerLocation;
    private String ownerPhone;
    private String treatsOwn;
    private String acceptsFromOthers;
    private String unp;
}

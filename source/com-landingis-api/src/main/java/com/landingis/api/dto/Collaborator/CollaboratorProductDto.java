package com.landingis.api.dto.collaborator;

import com.landingis.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class CollaboratorProductDto extends ABasicAdminDto {
    private Long collaboratorId;
    private Long productId;
    private Integer kind;
    private Double value;
}

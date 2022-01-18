package com.landingis.api.dto.collaborator;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.product.ProductDto;
import lombok.Data;

@Data
public class CollaboratorProductDto extends ABasicAdminDto {
    private CollaboratorDto collaboratorDto;
    private ProductDto productDto;
    private Integer kind;
    private Double value;
}

package com.landingis.api.form.import_export;

import com.landingis.api.storage.model.Category;
import com.landingis.api.validation.CategoryKind;
import com.landingis.api.validation.ImportExportKind;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateImportExportForm {
    @NotEmpty(message = "code cannot be null")
    @ApiModelProperty(required = true)
    private String code;

    private String filePath;

    @Min(0)
    @NotNull(message = "money cannot be null")
    @ApiModelProperty(required = true)
    private Double money;

    private String note;

    @ImportExportKind
    @NotNull(message = "kind cannot be null")
    @ApiModelProperty(required = true)
    private Integer kind;

    @NotNull(message = "categoryId cannot be null")
    @ApiModelProperty(required = true)
    private Long categoryId;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;
}

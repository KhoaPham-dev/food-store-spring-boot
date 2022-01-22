package com.landingis.api.form.import_export;

import com.landingis.api.validation.ImportExportKind;
import com.landingis.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateImportExportForm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String filePath;

    @Min(0)
    @NotNull(message = "money cannot be null")
    @ApiModelProperty(required = true)
    private Double money;

    private String note;

    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    @Status
    private Integer status;
}

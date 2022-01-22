package com.landingis.api.dto.import_export;

import com.landingis.api.dto.ABasicAdminDto;
import com.landingis.api.dto.account.AccountDto;
import com.landingis.api.dto.category.CategoryDto;
import lombok.Data;

@Data
public class ImportExportDto extends ABasicAdminDto {
    private String code;
    private String filePath;
    private Double money;
    private String note;
    private Integer kind;
    private CategoryDto categoryDto;
    private AccountDto accountDto;
}

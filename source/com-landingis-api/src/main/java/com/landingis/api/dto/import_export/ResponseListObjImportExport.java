package com.landingis.api.dto.import_export;

import com.landingis.api.dto.ResponseListObj;
import lombok.Data;

@Data
public class ResponseListObjImportExport extends ResponseListObj<ImportExportDto> {
    private Double sum;
}

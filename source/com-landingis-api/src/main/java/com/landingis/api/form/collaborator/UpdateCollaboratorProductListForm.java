package com.landingis.api.form.collaborator;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class UpdateCollaboratorProductListForm {
    private List<@Valid  UpdateCollaboratorProductForm> updateCollaboratorProductFormList;
}

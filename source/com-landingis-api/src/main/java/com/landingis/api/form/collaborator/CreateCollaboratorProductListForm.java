package com.landingis.api.form.collaborator;

import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class CreateCollaboratorProductListForm {
    private List<@Valid CreateCollaboratorProductForm> createCollaboratorProductFormList;
}

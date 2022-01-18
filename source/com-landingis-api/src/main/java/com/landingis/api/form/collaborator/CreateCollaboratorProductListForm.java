package com.landingis.api.form.collaborator;

import lombok.Data;

import java.util.List;

@Data
public class CreateCollaboratorProductListForm {
    private List<CreateCollaboratorProductForm> createCollaboratorProductFormList;
}

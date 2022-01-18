package com.landingis.api.form.collaborator;

import com.landingis.api.storage.model.CollaboratorProduct;
import lombok.Data;

import java.util.List;

@Data
public class DeleteCollaboratorProductListForm {
    private List<DeleteCollaboratorProductForm> deleteCollaboratorProductFormList;
}

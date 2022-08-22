package com.researchspace.dryad.model;

import lombok.Data;

/**
 * Represents related work stored within Dryad.
 */
@Data
public class DryadRelatedWork {

    private String relationship;
    private String identifier;
    private String identifierType;

}

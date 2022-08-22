package com.researchspace.dryad.model;


import lombok.Data;

/**
 * Represents an Author of the work stored in Dryad.
 */
@Data
public class DryadAuthor {

    private String firstName;
    private String lastName;
    private String email;
    private String affiliation;
    private String affiliationROR;
    private String affiliationISNI;
    private String orcid;
    private int order;

}


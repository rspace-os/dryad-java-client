package model;

import lombok.Data;


/**
 * Represents a funder of the work stored within Dryad.
 */
@Data
public class DryadFunder {

    private String organization;
    private String awardNumber;
    private String identifier;
    private String identifierType;


}

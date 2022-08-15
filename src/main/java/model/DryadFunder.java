package model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 * Represents a funder of the work stored within Dryad.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DryadFunder {

    // Name here to deserialize funder name from crossref api
    @JsonAlias("name")
    private String organization;
    private String awardNumber;
    // uri here to deserialize funder uri from crossref api
    @JsonAlias("uri")
    private String identifier;
    private String identifierType;


}

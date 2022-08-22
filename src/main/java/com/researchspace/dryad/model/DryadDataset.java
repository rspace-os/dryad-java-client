package com.researchspace.dryad.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.researchspace.dryad.model.location.DryadGeoLocation;

import java.util.List;

@Data
public class DryadDataset {

    @JsonProperty("_links")
    private DryadLinks links;
    private String identifier;
    private Integer id;
    private Long storageSize;
    private String relatedPublicationISSN;
    private String title;
    private List<DryadAuthor> authors;
    @JsonProperty("abstract")
    private String dryadAbstract;
    private List<String> keywords;
    private String usageNotes;
    private List<DryadGeoLocation> locations;
    private List<DryadRelatedWork> relatedWorks;
    private Integer versionNumber;
    private String versionStatus;
    private String curationStatus;
    private String versionChanges;
    private String publicationDate;
    private String lastModificationDate;
    private String visibility;
    private String sharingLink;
    private String editLink;
    private Integer userId;
    private String license;

}

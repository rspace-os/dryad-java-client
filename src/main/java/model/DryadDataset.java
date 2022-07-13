package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DryadDataset {

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
    private Integer userId;
    private String license;

}

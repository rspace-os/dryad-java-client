package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Dryad Submission object which can be used to create a new submission.
 */
@Data
@NoArgsConstructor
public class DryadSubmission {

    private String title;
    private List<DryadAuthor> authors;
    @JsonProperty("abstract")
    private String dryadAbstract;
    private List<DryadFunder> funders;
    private List<String> keywords;
    private String methods;
    private String usageNotes;
    private List<DryadRelatedWork> relatedWorks;



}

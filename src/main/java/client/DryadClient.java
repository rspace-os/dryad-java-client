package client;

import model.DryadDataset;
import model.DryadFile;
import model.DryadSubmission;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DryadClient {

    // Datasets
    DryadSubmission createSubmission(DryadSubmission submission);

    List<DryadDataset> getDatasets();

    DryadDataset getDataset(String doi);

    DryadDataset updateDataset(String doi);

    // File operations
    DryadFile stageFile(String doi, String filename, MultipartFile file);

    DryadFile stageFile(String doi, String url);



}

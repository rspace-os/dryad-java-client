package client;

import model.DryadDataset;
import model.DryadFile;
import model.DryadSubmission;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DryadClient {


    /**
     * Test connection to Dryad API.
     * @return true if connection is successful.
     */
    boolean testConnection();

    /**
     * Create a Dryad submission.
     * @param submission The Dryad submission to create.
     * @return The created Dryad submission.
     */
    DryadDataset createSubmission(DryadSubmission submission);

    /**
     * Get all datasets
     * @return List of DryadDataset
     */
    List<DryadDataset> getDatasets();

    /**
     * Get a dataset by its DOI.
     * @param doi The DOI of the dataset.
     * @return The dataset.
     */
    DryadDataset getDataset(String doi);

    /**
     * Update a dataset.
     * @param doi The dataset's DOI.
     * @param dryadDataset The dataset to update.
     * @return The updated dataset.
     */
    DryadDataset updateDataset(String doi, DryadDataset dryadDataset);

    /**
     * Upload a file to a Dryad dataset.
     * @param doi the Dryad dataset DOI
     * @param filename the filename of the file to upload
     * @param file the file to upload
     * @return the DryadFile object representing the uploaded file.
     */
    DryadFile stageFile(String doi, String filename, File file) throws IOException;

    /**
     * Attach a file to a Dryad dataset by its url.
     * @param doi The Dryad dataset's doi.
     * @param url The url of the file to attach.
     * @return The DryadFile object representing the file.
     */
    DryadFile stageFile(String doi, String url);





}

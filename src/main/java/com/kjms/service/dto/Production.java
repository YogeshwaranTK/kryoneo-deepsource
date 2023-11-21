package com.kjms.service.dto;

import java.util.List;

public class Production {

    List<ProductionContributor> productionContributors;
    List<ProductionDiscussion> productionDiscussions;
    List<ProductionReadyFile> productionReadyFiles;
    List<ProductionCompletedFile> productionCompletedFiles;

    public List<ProductionContributor> getProductionContributors() {
        return productionContributors;
    }

    public void setProductionContributors(List<ProductionContributor> productionContributors) {
        this.productionContributors = productionContributors;
    }

    public List<ProductionDiscussion> getProductionDiscussions() {
        return productionDiscussions;
    }

    public void setProductionDiscussions(List<ProductionDiscussion> productionDiscussions) {
        this.productionDiscussions = productionDiscussions;
    }

    public List<ProductionDiscussion> getDiscussions() {
        return productionDiscussions;
    }

    public void setDiscussions(List<ProductionDiscussion> productionDiscussions) {
        this.productionDiscussions = productionDiscussions;
    }

    public List<ProductionReadyFile> getProductionReadyFiles() {
        return productionReadyFiles;
    }

    public void setProductionReadyFiles(List<ProductionReadyFile> productionReadyFiles) {
        this.productionReadyFiles = productionReadyFiles;
    }

    public List<ProductionCompletedFile> getProductionCompletedFiles() {
        return productionCompletedFiles;
    }

    public void setProductionCompletedFiles(List<ProductionCompletedFile> productionCompletedFiles) {
        this.productionCompletedFiles = productionCompletedFiles;
    }
}

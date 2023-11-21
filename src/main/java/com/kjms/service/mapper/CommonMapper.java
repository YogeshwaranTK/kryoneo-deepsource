package com.kjms.service.mapper;


import com.kjms.domain.EntityCategory;
import com.kjms.domain.EntityFileType;
import com.kjms.service.dto.Category;
import com.kjms.service.dto.FileType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Mapper class for Common Api
 */
@Service
public class CommonMapper {

    private Category entityCategoryToCategoryTreeNode(EntityCategory entityCategory) {

        Category category = new Category();

        category.setId(entityCategory.getId());

        category.setName(entityCategory.getName());

        return category;
    }

    public List<Category> entityCategoriesToCategoryTreeNodes(List<EntityCategory> entityCategories) {
        return entityCategories.stream().map(this::entityCategoryToCategoryTreeNode)
            .collect(Collectors.toList());
    }

    public Page<FileType> entityFileTypeToFileTypeDto(Page<EntityFileType> fileTypes) {

        return fileTypes.map(entityFileType -> {

            FileType fileType = new FileType();

            fileType.setId(entityFileType.getId());

            fileType.setName(entityFileType.getName());

            return fileType;
        });
    }

    public FileType entityFilesTypeToFileType(EntityFileType entityFileType) {

            FileType fileType = new FileType();

            fileType.setId(entityFileType.getId());

            fileType.setName(entityFileType.getName());

            return fileType;

    }

    public List<FileType> entityFilesTypeToFileTypes(List<EntityFileType> fileTypes) {
        return fileTypes.stream().map(entityFileType -> {
            FileType fileType = new FileType();

            fileType.setId(entityFileType.getId());
            fileType.setName(entityFileType.getName());

            return fileType;
        }).collect(Collectors.toList());
    }

}

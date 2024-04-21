package com.kartishan.bookscroll.service;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GridFsService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    public String storeFile(MultipartFile file) throws IOException {
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return fileId.toString();
    }

    public GridFSFile retrieveFile(String fileId) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
    }

    public InputStreamResource downloadFileAsResource(String fileId) {
        GridFSFile gridFSFile = retrieveFile(fileId);
        if (gridFSFile != null) {
            GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);
            try {
                return new InputStreamResource(gridFsResource.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException("Error while retrieving file with ID: " + fileId, e);
            }
        } else {
            throw new RuntimeException("File with ID: " + fileId + " not found");
        }
    }
}

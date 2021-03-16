package com.zahjava.ecommercebackend.controller;

import com.zahjava.ecommercebackend.annotation.ApiController;
import com.zahjava.ecommercebackend.service.DocumentService;
import com.zahjava.ecommercebackend.utils.UrlConstraint;
import com.zahjava.ecommercebackend.view.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ApiController
@RequestMapping(UrlConstraint.DocumentManagement.ROOT)
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(UrlConstraint.DocumentManagement.CREATE)
    public Response create(@RequestParam(name = "files") MultipartFile [] files, @RequestParam(name = "entityName") String entityName, @RequestParam(name = "entityId") Long entityId, HttpServletRequest request, HttpServletResponse response) {
        return documentService.create(files, entityName, entityId);
    }

    /*@PutMapping(UrlConstraint.DocumentManagement.UPDATE)
    public Response updateBranch(@PathVariable Long id, @RequestParam(name = "files") MultipartFile [] files, @RequestParam(name = "entityName") String entityName, @RequestParam(name = "entityId") Long entityId, HttpServletRequest request, HttpServletResponse response) {
        return documentService.update(id, files, entityName, entityId);
    }*/

    @GetMapping(UrlConstraint.DocumentManagement.GET)
    public Response get(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return documentService.get(id);
    }
    @GetMapping(UrlConstraint.DocumentManagement.GET_ALL_BY_ENTITY)
    public Response getAllByDomain(@RequestParam Long entityId, @RequestParam String entityName, HttpServletRequest request, HttpServletResponse response) {
        return documentService.getAllByDomain(entityId, entityName);
    }
    @DeleteMapping(UrlConstraint.DocumentManagement.DELETE)
    public Response delete(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        return documentService.delete(id);
    }
    @GetMapping(UrlConstraint.DocumentManagement.GET_ALL)
    public Response getAll(HttpServletResponse httpServletResponse, Pageable pageable,
                           @RequestParam(value = "export", defaultValue = "false") boolean isExport,
                           @RequestParam(value = "search", defaultValue = "") String search,
                           @RequestParam(value = "status", defaultValue = "") String status) {

        Response response = documentService.getAll(pageable, isExport, search, status);
        httpServletResponse.setStatus(response.getStatusCode());
        return response;
    }
}

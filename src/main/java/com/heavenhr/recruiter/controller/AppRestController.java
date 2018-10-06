package com.heavenhr.recruiter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "App", description = "App REST controller", tags = {"App"})
public class AppRestController {

    private final MessageSourceAccessor messages;

    @Autowired
    public AppRestController(MessageSourceAccessor messages) {
        this.messages = messages;
    }

    @GetMapping(
            path = "/",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiOperation("Welcome index message")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok(messages.getMessage("welcome.msg", LocaleContextHolder.getLocale()));
    }

    @GetMapping(
            path = "/rest/v1/locale",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ApiOperation("Returns a simple locale message")
    public ResponseEntity<String> localeMessage() {
        return ResponseEntity.ok(messages.getMessage("locale.msg", LocaleContextHolder.getLocale()));
    }


}

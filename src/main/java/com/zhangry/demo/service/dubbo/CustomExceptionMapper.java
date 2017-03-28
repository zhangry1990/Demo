package com.zhangry.demo.service.dubbo;

import com.thinvent.common.rest.RestResponse;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by zhangry on 2017/3/28.
 */
public class CustomExceptionMapper implements ExceptionMapper<Exception> {
    public CustomExceptionMapper() {
    }

    public Response toResponse(Exception e) {
        RestResponse restResponse = RestResponse.failure(e.getMessage());
        return e instanceof NotFoundException?Response.status(Status.NOT_FOUND).entity(restResponse).type("application/json; charset=UTF-8").build():Response.status(Status.INTERNAL_SERVER_ERROR).entity(restResponse).type("application/json; charset=UTF-8").build();
    }
}

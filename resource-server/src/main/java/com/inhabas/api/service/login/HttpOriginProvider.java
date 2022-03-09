package com.inhabas.api.service.login;

import javax.servlet.http.HttpServletRequest;

public interface HttpOriginProvider {

    StringBuffer getOrigin(HttpServletRequest request);

}

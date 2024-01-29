package com.inhabas.api.auth.domain.error.authException;

public interface AuthExceptionCodes {

  /**
   * {@code invalid_request} - The request is missing a required parameter, includes an invalid
   * parameter value, includes a parameter more than once, or is otherwise malformed.
   */
  String INVALID_REQUEST = "invalid_request";

  /** {@code access_denied} - The resource owner or authorization server denied the request. */
  String ACCESS_DENIED = "access_denied";

  /**
   * {@code invalid_token} - The access token provided is expired, revoked, malformed, or invalid
   * for other reasons. The resource SHOULD respond with the HTTP 401 (Unauthorized) status code.
   * The client MAY request a new access token and retry the protected resource request.
   *
   * @see <a href="https://tools.ietf.org/html/rfc6750#section-3.1">RFC-6750 - Section 3.1 - Error
   *     Codes</a>
   */
  String INVALID_TOKEN = "invalid_token";

  /**
   * {@code temporarily_unavailable} - The authorization server is currently unable to handle the
   * request due to a temporary overloading or maintenance of the server. (This error code is needed
   * because a 503 Service Unavailable HTTP status code cannot be returned to the client via an HTTP
   * redirect.)
   */
  String TEMPORARILY_UNAVAILABLE = "temporarily_unavailable";

  /**
   * {@code unsupported_token_type} - The authorization server does not support the revocation of
   * the presented token type.
   *
   * @see <a href="https://tools.ietf.org/html/rfc7009#section-2.2.1">RFC-7009 - Section 2.2.1 -
   *     Error Response</a>
   */
  String UNSUPPORTED_TOKEN_TYPE = "unsupported_token_type";
}

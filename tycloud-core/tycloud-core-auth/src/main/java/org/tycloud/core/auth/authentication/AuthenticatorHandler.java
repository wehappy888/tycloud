
package org.tycloud.core.auth.authentication;

import org.tycloud.core.auth.enumeration.IdType;
import org.tycloud.core.auth.face.model.AuthModel;
import org.tycloud.core.auth.face.model.LoginInfoModel;
import org.tycloud.core.foundation.enumeration.UserType;

/**
 * Created by yaohelang on 2017/9/20.
 */
public interface AuthenticatorHandler {
    
     LoginInfoModel doAuthenticate(IdType idType, UserType userType, AuthModel authModel) throws Exception;
}

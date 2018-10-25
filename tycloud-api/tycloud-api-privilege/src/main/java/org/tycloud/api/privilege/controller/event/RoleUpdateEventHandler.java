package org.tycloud.api.privilege.controller.event;

import org.springframework.stereotype.Component;
import org.tycloud.api.privilege.face.model.RoleModel;
import org.tycloud.component.event.RestEvent;
import org.tycloud.component.event.RestEventHandler;
import org.tycloud.core.foundation.utils.ValidationUtil;

@Component("roleUpdateEventHandler")
public class RoleUpdateEventHandler extends RestEventHandler
{

    public  void handleEvent(RestEvent restEvent) throws Exception
    {
        RoleModel roleModel  = (RoleModel) restEvent.getSource();


        if(!ValidationUtil.isEmpty(roleModel))

        {
            //事件机制示例
        }

    }


}

package com.nsntc.zuul.container;

import com.nsntc.zuul.config.yml.GlobalYml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Class Name: GeneralPermissionsContainer
 * Package: com.nsntc.zuul.container
 * Description: 通用权限
 * @author wkm
 * Create DateTime: 2018/3/6 下午3:21
 * Version: 1.0
 */
@Component
public class GeneralPermissionsContainer {

    @Autowired
    private GlobalYml globalYml;

    /**
     * 通用权限
     */
    private static List<String> generalPermissionList;

    /**
     * Method Name: initGeneralPermission
     * Description: 初始化通用权限
     * Create DateTime: 2018/2/12 上午6:32
     */
    public void initGeneralPermission() {
        synchronized (GeneralPermissionsContainer.class) {
            if (StringUtils.isNotEmpty(this.globalYml.getGeneralPermissions())) {
                String[] generalPermissions = StringUtils.split(this.globalYml.getGeneralPermissions(), ',');
                for (int i = 0, len = generalPermissions.length; i < len; i++) {
                    generalPermissions[i] = StringUtils.trim(generalPermissions[i]);
                }
                generalPermissionList = Arrays.asList(generalPermissions);
            }
        }
    }

    public static List<String> getGeneralPermissionList() {
        synchronized (GeneralPermissionsContainer.class) {
            return generalPermissionList;
        }
    }
}

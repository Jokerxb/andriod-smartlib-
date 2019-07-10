package com.njupt.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class getPropertiesUtil {
    /**
     * 得到config.properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */
    public static Properties getNetConfigProperties() {
        Properties props = new Properties();
        InputStream in = getPropertiesUtil.class.getResourceAsStream("/assets/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}

package com.lnson.simple.projname.serviceLauncher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;

/**
 * 应用层服务宿主(启动)程序
 *
 * @author 远成集团信息部
 * @version 1.0.0 2018-01-10 19:51:24
 */
public class ServiceLauncher {

    protected static Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        try {
            System.out.println("=======================");
            System.out.println("        服务启动                    ");
            SystemDetails.outputDetails();
            System.out.println("=======================");

            System.out.println("         绑定IP        ");
            getLocalip();

            System.out.println("     开始初始化服务            ");
            BeanFactoryUtil.init();

            System.out.println("=======================");
            System.out.println("      服务初始化完成           ");
            System.out.println("=======================");
            Thread.sleep(1000L * 60L * 60L * 24L * 365L * 100L);
        } catch (Exception e) {
            logger.fatal(MessageFormat.format("服务启动时发生错误：{0}", e));
        }
    }

    private static void getLocalip() {
        try {
            System.out.println("服务绑定的ip: " + java.net.InetAddress.getLocalHost().getHostAddress());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

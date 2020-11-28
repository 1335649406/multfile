package jhw.multifile.Main;

import source_discovery.center.RegistryCenter;

/**
 * @Author: wjh
 * @DateTime: 2020/7/31 11:43
 * @Description: TODO
 */
public class RegistryCenterMain {
    public static void main(String[] args) {
        RegistryCenter center = new RegistryCenter();
        center.setPort(54170);
        System.out.println("注册中心开始侦听");
        center.startup();
    }
}

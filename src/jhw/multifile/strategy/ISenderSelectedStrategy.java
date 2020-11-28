package jhw.multifile.strategy;

import balance.DefaultNetNode;

import java.util.List;

/**
 * @Author: wjh
 * @DateTime: 2020/7/30 19:42
 * @Description: TODO
 */
public interface ISenderSelectedStrategy {
    List<DefaultNetNode> senderSelected(List<DefaultNetNode> addressList);
}

package kimble.connection.serverside.clientloading;

import java.util.List;
import kimble.connection.serverside.KimbleClientInfo;

/**
 *
 * @author Christoffer
 */
public interface LoadClientsInterface {

    public List<KimbleClientInfo> loadInfoList();
}

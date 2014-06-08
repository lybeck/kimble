package kimble.connection.serverside;

import java.util.List;
import kimble.connection.serverside.clientloading.StartJarInterface;

/**
 *
 * @author Christoffer
 */
class LoadTournamentClients implements StartJarInterface {

    private final List<KimbleClientInfo> clientInfo;

    public LoadTournamentClients(List<KimbleClientInfo> clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public List<KimbleClientInfo> jarStartInfo() {
        return clientInfo;
    }
}

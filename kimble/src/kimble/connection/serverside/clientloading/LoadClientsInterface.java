/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

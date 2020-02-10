/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author namnh16
 */
public class HUpgradeModel extends BaseModel{
    private static final Logger _Logger = ZLogger.getLogger(HUpgradeModel.class);
    public static final HUpgradeModel INSTANCE = new HUpgradeModel();
    
    private final ThriftServers.Config _config = new ThriftServers.Config();
    private final String _name = "Application";
    
    private HUpgradeModel(){
        
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);
        
        _config.host = ZConfig.Instance.getString(ThriftServers.class, _name, "host", "127.0.0.1");
        _config.port = ZConfig.Instance.getInt(ThriftServers.class, _name, "port", 8089);
        
        User user = (User) request.getSession(false).getAttribute("user");
        
        if(user == null){
//            out.println("Must log in first");
        }
        /* Switch to this block if servers are running on top of HTTPS
        else{
            TSSLTransportFactory.TSSLTransportParameters params =
                new TSSLTransportFactory.TSSLTransportParameters();
            params.setTrustStore("src/main/resources/truststore.jks", "password");
            
            try (TTransport socket = TSSLTransportFactory.getClientSocket(
                    "localhost", 8090, 500000, params);
            ) {
                TBinaryProtocol binProto = new TBinaryProtocol(socket);
                TMultiplexedProtocol mulProto = new TMultiplexedProtocol(
                        binProto, "App"
                );

                App.Client appClient = new App.Client(mulProto);

                session.setAttribute("user", appClient.upgrade(user));
                
                response.sendRedirect("/user/info");
            } catch (DatabaseException ex) {
                
            } catch (TException ex) {
                java.util.logging.Logger.getLogger(ShowInfoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
        else{
            try{
//              TBinaryProtocol.Factory binProtFactory = new TBinaryProtocol.Factory();
                Application.Client.Factory appClientFactory = new Application.Client.Factory();
                TClientPoolConfig.ConnConfig connConfig = new TClientPoolConfig.ConnConfig(
                    _config.host, _config.port, _config.framed, false, 50000, _config.maxFrameSize, _config.encryptVersionPriority
                );

                TClientFactory clientFactory = new TClientFactory(appClientFactory, connConfig);
                Application.Client appClient = (Application.Client) clientFactory.makeObject();

                request.getSession(false).setAttribute("user", appClient.upgrade(user));

                clientFactory.destroyObject(appClient);
                response.sendRedirect("/user/info");
            }
            catch(InvalidTokenException ex){
                _Logger.error(ex.getMessage(), ex);
            }
            catch(IOException | TException ex){
                _Logger.error(ex.getMessage(), ex);
            }
            catch(Exception ex){
                _Logger.error(ex.getMessage(), ex);
            }
            finally{
                Profiler.closeThreadProfiler();
            }
        }
    }
}

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;
import java.io.IOException;
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
public class HRemoveAccountModel extends BaseModel{
    private static final Logger _Logger = ZLogger.getLogger(HRemoveAccountModel.class);
    public static final HRemoveAccountModel INSTANCE = new HRemoveAccountModel();
    
    private final ThriftServers.Config _config = new ThriftServers.Config();
    private final String _name = "Account";
    
    private HRemoveAccountModel(){
        
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);
        
        _config.host = ZConfig.Instance.getString(ThriftServers.class, _name, "host", "127.0.0.1");
        _config.port = ZConfig.Instance.getInt(ThriftServers.class, _name, "port", 8090);
        
        User user = (User) request.getSession(false).getAttribute("user");
        
        if(user == null){
//            out.println("Must log in first");
        }
        else if(user.getType() != UserType.ADMIN){
//            out.println("Reserved for ADMIN");
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
                        binProto, "Account"
                );

                Account.Client accClient = new Account.Client(mulProto);

                if(!"".equals(request.getParameter("id"))){
                    accClient.remove(Integer.parseInt(request.getParameter("id")));
                }
                
                response.sendRedirect("/user/info");
            }
            catch (InvalidTokenException ex){
                
            }
            catch (TException ex) {
                Logger.getLogger(LogInServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
        else{
            try{
                Account.Client.Factory accClientFactory = new Account.Client.Factory();
                TClientPoolConfig.ConnConfig connConfig = new TClientPoolConfig.ConnConfig(
                    _config.host, _config.port, _config.framed, false, 50000, _config.maxFrameSize, _config.encryptVersionPriority
                );

                TClientFactory clientFactory = new TClientFactory(accClientFactory, connConfig);
                Account.Client accClient = (Account.Client) clientFactory.makeObject();
                
                if(!"".equals(request.getParameter("id"))){
                    accClient.remove(Integer.parseInt(request.getParameter("id")));
                }
                
                response.sendRedirect("/user/info");
            }
            catch(InvalidTokenException ex){
                
            }
            catch(TException | IOException ex){
                _Logger.error(ex.getMessage(), ex);
            }
            catch(Exception ex){
                _Logger.error(ex.getMessage(), ex);
            }
        }
    }
}

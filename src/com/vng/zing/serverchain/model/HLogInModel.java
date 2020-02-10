/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;
import com.vng.zing.thriftserver.ThriftServers.Config;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 *
 * @author namnh16
 */
public class HLogInModel extends BaseModel{
    private static final Logger _Logger = ZLogger.getLogger(HLogInModel.class);
    public static final HLogInModel INSTANCE = new HLogInModel();
    
    private final Config _config = new Config();
    private final String _name = "Authenticator";
    
    private HLogInModel(){
        
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);
        
        _config.host = ZConfig.Instance.getString(ThriftServers.class, _name, "host", "127.0.0.1");
        _config.port = ZConfig.Instance.getInt(ThriftServers.class, _name, "port", 8088);
        
        /* Switch to this block if servers are running on top of HTTPS
        TSSLTransportFactory.TSSLTransportParameters params =
            new TSSLTransportFactory.TSSLTransportParameters();
        params.setTrustStore("src/main/resources/truststore.jks", "password");
        
        try (TTransport socket = TSSLTransportFactory.getClientSocket(
                "localhost", 8090, 500000, params);
            ) {
            TBinaryProtocol binProto = new TBinaryProtocol(socket);
            TMultiplexedProtocol mulProto = new TMultiplexedProtocol(
                    binProto, "Authenticator"
            );
            
            Authenticator.Client authClient = new Authenticator.Client(mulProto);
            
            User user = authClient.authenticate(
                    request.getParameter("username"),
                    request.getParameter("password")
            );
            
            request.getSession(true).setAttribute("user", user);
            
            response.sendRedirect("/user/info");
        } catch (InvalidTokenException ex){
            response.sendRedirect("/");
        } catch (TTransportException | DatabaseException ex) {
            java.util.logging.Logger.getLogger(LogInServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TException ex) {
            java.util.logging.Logger.getLogger(LogInServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        try{
//            TBinaryProtocol.Factory binProtFactory = new TBinaryProtocol.Factory();
            Authenticator.Client.Factory authClientFactory = new Authenticator.Client.Factory();
            TClientPoolConfig.ConnConfig connConfig = new TClientPoolConfig.ConnConfig(
                _config.host, _config.port, _config.framed, false, 50000, _config.maxFrameSize, _config.encryptVersionPriority
            );
            
            TClientFactory clientFactory = new TClientFactory(authClientFactory, connConfig);
            Authenticator.Client authClient = (Authenticator.Client) clientFactory.makeObject();
            
            User user = authClient.authenticate(
                request.getParameter("username"),
                request.getParameter("password")
            );
            
            request.getSession(true).setAttribute("user", user);
            
            clientFactory.destroyObject(authClient);
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

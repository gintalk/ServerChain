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
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

/**
 *
 * @author namnh16
 */
public class HAddAccountModel extends BaseModel{
    private static final Logger _Logger = ZLogger.getLogger(HAddAccountModel.class);
    public static final HAddAccountModel INSTANCE = new HAddAccountModel();
    
    private final ThriftServers.Config _config = new ThriftServers.Config();
    private final String _name = "Account";
    
    private HAddAccountModel(){
        
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);
        
        _config.host = ZConfig.Instance.getString(ThriftServers.class, _name, "host", "127.0.0.1");
        _config.port = ZConfig.Instance.getInt(ThriftServers.class, _name, "port", 8090);
        
        /* Switch to this block if servers are running on top of HTTPS
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
                        
            Token token = new Token();
            token.setFieldValue(token.fieldForId(1), request.getParameter("username"));
            token.setFieldValue(token.fieldForId(2), request.getParameter("password"));

            User user = new User();
            user.setFieldValue(user.fieldForId(2), request.getParameter("name"));

            accClient.add(token, user);
        }
        catch (InvalidTokenException ex){
        }
        catch (TException ex) {
            Logger.getLogger(LogInServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        try{
            Account.Client.Factory accClientFactory = new Account.Client.Factory();
            TClientPoolConfig.ConnConfig connConfig = new TClientPoolConfig.ConnConfig(
                _config.host, _config.port, _config.framed, false, 50000, _config.maxFrameSize, _config.encryptVersionPriority
            );

            TClientFactory clientFactory = new TClientFactory(accClientFactory, connConfig);
            Account.Client accClient = (Account.Client) clientFactory.makeObject();

            Token token = new Token();
            token.setFieldValue(token.fieldForId(1), request.getParameter("username"));
            token.setFieldValue(token.fieldForId(2), request.getParameter("password"));

            User user = new User();
            user.setFieldValue(user.fieldForId(2), request.getParameter("name"));

            accClient.add(token, user);
            
            response.sendRedirect("/");
        }
        catch(TException ex){
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

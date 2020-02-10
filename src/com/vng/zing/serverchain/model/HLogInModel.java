/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.thriftpool.TClientFactory;

/**
 *
 * @author namnh16
 */
public class HLogInModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(HLogInModel.class);
    public static final HLogInModel INSTANCE = new HLogInModel();
    private static final String _serviceName = "Authenticator";

    private HLogInModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

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
            try{
                response.sendRedirect("/user/info");
            }
            catch(IOException ioEx){
                _Logger.error(ioEx.getMessage(), ioEx);
            }
        } catch (TTransportException | DatabaseException ex) {
            _Logger.error(ex.getMessage(), ex);
        } catch (TException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
         */
        try {
//            TBinaryProtocol.Factory binProtFactory = new TBinaryProtocol.Factory();
            TClientFactory clientFactory = new TClientFactory(
                    new Authenticator.Client.Factory(),
                    this.getConnectionConfig(_serviceName)
            );
            Authenticator.Client authClient = (Authenticator.Client) clientFactory.makeObject();

            User user = authClient.authenticate(
                    request.getParameter("username"),
                    request.getParameter("password")
            );

            request.getSession(true).setAttribute("user", user);

            clientFactory.destroyObject(authClient);
            response.sendRedirect("/user/info");
        } catch (InvalidTokenException ex) {
            try {
                response.sendRedirect("/");
            } catch (IOException ioEx) {
                _Logger.error(ioEx.getMessage(), ioEx);
            }
        } catch (TException ex) {
            _Logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}

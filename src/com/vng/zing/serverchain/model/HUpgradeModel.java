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
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.thriftpool.TClientFactory;

/**
 *
 * @author namnh16
 */
public class HUpgradeModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(HUpgradeModel.class);
    public static final HUpgradeModel INSTANCE = new HUpgradeModel();
    private static final String _serviceName = "Application";

    private HUpgradeModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        User user = (User) request.getSession(false).getAttribute("user");
        if (user == null) {
//            out.println("Must log in first");
        } /* Switch to this block if servers are running on top of HTTPS
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
        }*/ else {
            try {
                TClientFactory clientFactory = new TClientFactory(
                        new Application.Client.Factory(),
                        this.getConnectionConfig(_serviceName)
                );
                Application.Client appClient = (Application.Client) clientFactory.makeObject();

                request.getSession(false).setAttribute("user", appClient.upgrade(user));

                clientFactory.destroyObject(appClient);
                response.sendRedirect("/user/info");
            } catch (InvalidTokenException ex) {
                try {
                    response.sendRedirect("/user/info");
                } catch (IOException ioEx) {
                    _Logger.error(ioEx.getMessage(), ioEx);
                }
            } catch (TException ex) {
                _Logger.error(ex.getMessage(), ex);
            } catch (Exception ex) {
                _Logger.error(ex.getMessage(), ex);
            } finally {
//                Profiler.closeThreadProfiler();
            }
        }
    }
}

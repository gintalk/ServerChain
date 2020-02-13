/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Application;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.thriftpool.TClientFactory;

/**
 *
 * @author namnh16
 */
public class HUpgradeModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HUpgradeModel.class);
    public static final HUpgradeModel INSTANCE = new HUpgradeModel();
    private static final String SERVICE_NAME = "Application";

    private HUpgradeModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        User user = (User) request.getSession(false).getAttribute("user");
        if (user == null) {
            this.outAndClose(request, response, "Must log in first");
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
                    this.getConnectionConfig(SERVICE_NAME)
                );
                Application.Client appClient = (Application.Client) clientFactory.makeObject();

                request.getSession(false).setAttribute("user", appClient.upgrade(user));

                clientFactory.destroyObject(appClient);
                response.sendRedirect("/user/info");

            } catch (TZException ex) {
                this.outAndClose(request, response, ex.getWebMessage());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            } finally {
//                Profiler.closeThreadProfiler();
            }
        }
    }
}

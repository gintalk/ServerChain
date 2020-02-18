/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.TWriteService;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.serverchain.cache.IntStringCache;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HUpgradeModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HUpgradeModel.class);
    public static final HUpgradeModel INSTANCE = new HUpgradeModel();

    private HUpgradeModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            }
            else{
                TClientFactory clientFactory = new TClientFactory(
                    new TWriteService.Client.Factory(),
                    this.getConnectionConfig("WriteService")
                );
                TWriteService.Client client = (TWriteService.Client) clientFactory.makeObject();

                TUserResult updateResult = client.upgrade(user);
                if (updateResult.getError() != ECode.C_SUCCESS.getValue()) {
                    this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                } else {
                    IntStringCache.INSTANCE.remove(user.getId());
                    request.getSession(false).setAttribute("user", updateResult.getValue());
                    response.sendRedirect("/user/info");
                }

                clientFactory.destroyObject(client);
            }
            
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            this.outAndClose(request, response, "Unexpected error, please response");
        } finally {
//                Profiler.closeThreadProfiler();
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

                App.Client client = new App.Client(mulProto);

                session.setAttribute("user", client.upgrade(user));
                
                response.sendRedirect("/user/info");
            } catch (DatabaseException ex) {
                
            } catch (TException ex) {
                java.util.logging.Logger.getLogger(ShowInfoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/

    }
}

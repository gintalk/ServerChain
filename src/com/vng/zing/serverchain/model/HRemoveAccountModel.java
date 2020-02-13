/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.media.common.utils.ServletUtils;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.TZException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.thriftpool.TClientFactory;

/**
 *
 * @author namnh16
 */
public class HRemoveAccountModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HRemoveAccountModel.class);
    public static final HRemoveAccountModel INSTANCE = new HRemoveAccountModel();
    private static final String SERVICE_NAME = "Account";

    private HRemoveAccountModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        User user = (User) request.getSession(false).getAttribute("user");
        if (user == null) {
            this.outAndClose(request, response, "Must log in first");
        } else if (user.getType() != UserType.ADMIN) {
            this.outAndClose(request, response, "Reserved for ADMIN");
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
                        binProto, "Account"
                );

                Account.Client accClient = new Account.Client(mulProto);

                if(!"".equals(request.getParameter("id"))){
                    accClient.remove(Integer.parseInt(request.getParameter("id")));
                }
                
                response.sendRedirect("/user/info");
            }
            catch (InvalidTokenException ex){
                try{
                    response.sendRedirect("/user/info");
                }
                catch(IOException ioEx){
                    _Logger.error(ioEx.getMessage(), ioEx);
                }
            }
            catch (TException ex) {
                _Logger.error(ex.getMessage(), ex);
            }
        }
         */ else {
            try {
                TClientFactory clientFactory = new TClientFactory(
                    new Account.Client.Factory(),
                    this.getConnectionConfig(SERVICE_NAME)
                );
                Account.Client accClient = (Account.Client) clientFactory.makeObject();

                if (!"".equals(request.getParameter("id"))) {
                    accClient.remove(Integer.parseInt(
                        ServletUtils.getString(request, "id", "")));
                }
                response.sendRedirect("/user/info");

            } catch (TZException ex) {
                this.outAndClose(request, response, ex.getWebMessage());
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
    }
}

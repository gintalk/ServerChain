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
import com.vng.zing.resource.thrift.TI32Result;
import com.vng.zing.resource.thrift.TWriteService;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HRemoveAccountModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HRemoveAccountModel.class);
    public static final HRemoveAccountModel INSTANCE = new HRemoveAccountModel();

    private HRemoveAccountModel() {

    }

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) {
//        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);

        try {
            User user = (User) request.getSession(false).getAttribute("user");
            if (user == null) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.UNLOADED));
            } else if (user.getType() != UserType.ADMIN) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.NOT_ALLOW));
            } else {
                TClientFactory clientFactory = new TClientFactory(
                    new TWriteService.Client.Factory(),
                    this.getConnectionConfig("WriteService")
                );
                TWriteService.Client client = (TWriteService.Client) clientFactory.makeObject();

                if (!"".equals(ServletUtils.getString(request, "id", ""))) {
                    TI32Result updateResult = client.remove(Integer.parseInt(ServletUtils.getString(request, "id", "")));
                    if (updateResult.getError() != ECode.C_SUCCESS.getValue()) {
                        this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                    } else {
                        this.outAndClose(request, response, "Account removed");
                    }
                }

                clientFactory.destroyObject(client);
                response.sendRedirect("/user/info");
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
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

                Account.Client client = new Account.Client(mulProto);

                if(!"".equals(request.getParameter("id"))){
                    client.remove(Integer.parseInt(request.getParameter("id")));
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
        }*/

    }
}

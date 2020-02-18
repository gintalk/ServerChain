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
import com.vng.zing.thrift.resource.TI32Result;
import com.vng.zing.thrift.resource.TReadService;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.thrift.resource.TWriteService;
import com.vng.zing.thrift.resource.Token;
import com.vng.zing.thrift.resource.User;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HAddAccountModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HAddAccountModel.class);
    public static final HAddAccountModel INSTANCE = new HAddAccountModel();

    private HAddAccountModel() {

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
            try{
                response.sendRedirect("/user/info");
            }
            catch(IOException ioEx){
                LOGGER.error(ioEx.getMessage(), ioEx);
            }
        }
        catch (TException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
         */
        try {
            TClientFactory clientFactory = new TClientFactory(
                new TReadService.Client.Factory(),
                this.getConnectionConfig("ReadService")
            );
            TReadService.Client readClient = (TReadService.Client) clientFactory.makeObject();

            TUserResult queryResult = readClient.authenticate(ServletUtils.getString(request, "username", ""), "");
            if (queryResult.getError() == ECode.C_SUCCESS.getValue()) {
                this.outAndClose(request, response, MessageGenerator.getMessage(ECode.ALREADY_EXIST));
            } else {
                clientFactory.destroyObject(readClient);
                clientFactory = new TClientFactory(
                    new TWriteService.Client.Factory(),
                    this.getConnectionConfig("WriteService")
                );
                TWriteService.Client writeClient = (TWriteService.Client) clientFactory.makeObject();

                Token token = new Token();
                token.setUsername(ServletUtils.getString(request, "username", ""));
                token.setPassword(ServletUtils.getString(request, "password", ""));

                User user = new User();
                user.setName(ServletUtils.getString(request, "name", ""));

                TI32Result updateResult = writeClient.add(token, user);
                if (updateResult.getError() != ECode.C_SUCCESS.getValue()) {
                    this.outAndClose(request, response, MessageGenerator.getMessage(updateResult.getError()));
                } else {
                    response.sendRedirect("/");
                }

                clientFactory.destroyObject(writeClient);
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}

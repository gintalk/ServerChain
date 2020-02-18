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
import com.vng.zing.thrift.resource.TReadService;
import com.vng.zing.thrift.resource.TUserResult;
import com.vng.zing.serverchain.common.MessageGenerator;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.thrift.client.TClientPoolManager;
import com.vng.zing.thrift.client.TReadServiceClient;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.zcommon.thrift.ECode;

/**
 *
 * @author namnh16
 */
public class HLoginModel extends BaseModel {

    private static final Logger LOGGER = ZLogger.getLogger(HLoginModel.class);
    public static final HLoginModel INSTANCE = new HLoginModel();

    private HLoginModel() {

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
            
            Authenticator.Client client = new Authenticator.Client(mulProto);
            
            User user = client.authenticate(
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
            TClientFactory clientFactory = new TClientFactory(
                new TReadService.Client.Factory(),
                this.getConnectionConfig("ReadService")
            );
            TReadService.Client readClient = (TReadService.Client) clientFactory.makeObject();
            
//            TReadServiceClient readClient = TClientPoolManager.getReadServiceClient();

            String encrdPassword = Utils.md5(ServletUtils.getString(request, "password", ""));
            TUserResult queryResult = readClient.authenticate(
                ServletUtils.getString(request, "username", ""),
                encrdPassword
            );
            if (queryResult.getError() != ECode.C_SUCCESS.getValue()) {
                this.outAndClose(request, response, MessageGenerator.getMessage(queryResult.getError()));
            } else {
                request.getSession(true).setAttribute("user", queryResult.getValue());
                response.sendRedirect("/user/info");
            }

//            clientFactory.destroyObject(client);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);

            this.outAndClose(request, response, MessageGenerator.getMessage(ECode.EXCEPTION));
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}

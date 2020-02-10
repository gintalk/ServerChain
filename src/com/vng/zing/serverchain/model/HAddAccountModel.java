/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Account;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.Token;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.thriftpool.TClientFactory;

/**
 *
 * @author namnh16
 */
public class HAddAccountModel extends BaseModel {

    private static final Logger _Logger = ZLogger.getLogger(HAddAccountModel.class);
    public static final HAddAccountModel INSTANCE = new HAddAccountModel();
    private static final String _serviceName = "Account";

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
                _Logger.error(ioEx.getMessage(), ioEx);
            }
        }
        catch (TException ex) {
            _Logger.error(ex.getMessage(), ex);
        }
         */
        try {
            TClientFactory clientFactory = new TClientFactory(
                    new Account.Client.Factory(),
                    this.getConnectionConfig(_serviceName)
            );
            Account.Client accClient = (Account.Client) clientFactory.makeObject();

            Token token = new Token();
            token.setFieldValue(
                    token.fieldForId(1),
                    request.getParameter("username")
            );
            token.setFieldValue(
                    token.fieldForId(2),
                    request.getParameter("password")
            );

            User user = new User();
            user.setFieldValue(
                    user.fieldForId(2),
                    request.getParameter("name")
            );

            accClient.add(token, user);

            clientFactory.destroyObject(accClient);
            response.sendRedirect("/");
        } catch (InvalidTokenException ex) {
            this.outAndClose(request, response, "Something happened!");
        } catch (TException ex) {
            _Logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            _Logger.error(ex.getMessage(), ex);
        } finally {
//            Profiler.closeThreadProfiler();
        }
    }
}

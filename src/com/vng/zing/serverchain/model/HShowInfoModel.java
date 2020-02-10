/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.serverchain.model;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.resource.thrift.Authenticator;
import com.vng.zing.resource.thrift.InvalidTokenException;
import com.vng.zing.resource.thrift.User;
import com.vng.zing.resource.thrift.UserType;
import com.vng.zing.serverchain.utils.Utils;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.thriftpool.TClientFactory;
import com.vng.zing.thriftpool.TClientPoolConfig;
import com.vng.zing.thriftserver.ThriftServers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.rythmengine.Rythm;
import org.rythmengine.utils.NamedParams;

/**
 *
 * @author namnh16
 */
public class HShowInfoModel extends BaseModel{
    private static final Logger _Logger = ZLogger.getLogger(HShowInfoModel.class);
    public static final HShowInfoModel INSTANCE = new HShowInfoModel();
    
    private HShowInfoModel(){
        
    }
    
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response){
        ThreadProfiler profiler = Profiler.getThreadProfiler();
        this.prepareHeaderHtml(response);
        
        try{
            User user = (User) request.getSession(false).getAttribute("user");
            
            this.outAndClose(request, response, this.getInfoString(user));
        }
        catch(IOException ex){
            _Logger.error(ex.getMessage(), ex);
        }
        finally{
            Profiler.closeThreadProfiler();
        }
    }
    
    private String getInfoString(User user) throws IOException{        
        String id = String.valueOf(user.getFieldValue(user.fieldForId(1)));
        String name = (String) user.getFieldValue(user.fieldForId(2));
        String type = Utils.toStringFromUserType((UserType) user.getFieldValue(user.fieldForId(3)));
        String joinDate = (String) user.getFieldValue(user.fieldForId(4));
        String privilege = ("ADMIN".equals(type)) ? "Abuse your power!" : "Upgrade";
        String privilegePath = ("ADMIN".equals(type)) ? "/html/remove.html" : "/user/upgrade";
        String profilePic = 
            ("Tom".equals(name) || "Spike".equals(name) || "Jerry".equals(name) || "Toodles".equals(name))
            ? name + ".jpg" : "EricCartman.jpg";
                
        return Rythm.render("info.rythm", NamedParams.from(
                NamedParams.p("id", id),
                NamedParams.p("name", name),
                NamedParams.p("type", type),
                NamedParams.p("joinDate", joinDate),
                NamedParams.p("privilege", privilege),
                NamedParams.p("privilegePath", privilegePath),
                NamedParams.p("profilePic", profilePic)));
    }
}

/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.vng.zing.resource.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.scheme.TupleBinaryScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TTupleBinaryProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

  public interface Iface {

    public User upgrade(User user) throws TZException, org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void upgrade(User user, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client extends org.apache.thrift.TServiceClient implements Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      super(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
      super(iprot, oprot);
    }

    public User upgrade(User user) throws TZException, org.apache.thrift.TException
    {
      send_upgrade(user);
      return recv_upgrade();
    }

    public void send_upgrade(User user) throws org.apache.thrift.TException
    {
      upgrade_args args = new upgrade_args();
      args.setUser(user);
      sendBase("upgrade", args);
    }

    public User recv_upgrade() throws TZException, org.apache.thrift.TException
    {
      upgrade_result result = new upgrade_result();
      receiveBase(result, "upgrade");
      if (result.isSetSuccess()) {
        return result.success;
      }
      if (result.ex != null) {
        throw result.ex;
      }
      throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "upgrade failed: unknown result");
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void upgrade(User user, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
      checkReady();
      upgrade_call method_call = new upgrade_call(user, resultHandler, this, ___protocolFactory, ___transport);
      this.___currentMethod = method_call;
      ___manager.call(method_call);
    }

    public static class upgrade_call extends org.apache.thrift.async.TAsyncMethodCall {
      private User user;
      public upgrade_call(User user, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, false);
        this.user = user;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("upgrade", org.apache.thrift.protocol.TMessageType.CALL, 0));
        upgrade_args args = new upgrade_args();
        args.setUser(user);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public User getResult() throws TZException, org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
        return (new Client(prot)).recv_upgrade();
      }
    }

  }

  public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
    }

    protected Processor(I iface, Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends Iface> Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> getProcessMap(Map<String,  org.apache.thrift.ProcessFunction<I, ? extends  org.apache.thrift.TBase>> processMap) {
      processMap.put("upgrade", new upgrade());
      return processMap;
    }

    public static class upgrade<I extends Iface> extends org.apache.thrift.ProcessFunction<I, upgrade_args> {
      public upgrade() {
        super("upgrade");
      }

      public upgrade_args getEmptyArgsInstance() {
        return new upgrade_args();
      }

      protected boolean isOneway() {
        return false;
      }

      public upgrade_result getResult(I iface, upgrade_args args) throws org.apache.thrift.TException {
        upgrade_result result = new upgrade_result();
        try {
          result.success = iface.upgrade(args.user);
        } catch (TZException ex) {
          result.ex = ex;
        }
        return result;
      }
    }

  }

  public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class.getName());
    public AsyncProcessor(I iface) {
      super(iface, getProcessMap(new HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
    }

    protected AsyncProcessor(I iface, Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      super(iface, getProcessMap(processMap));
    }

    private static <I extends AsyncIface> Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase,?>> getProcessMap(Map<String,  org.apache.thrift.AsyncProcessFunction<I, ? extends  org.apache.thrift.TBase, ?>> processMap) {
      processMap.put("upgrade", new upgrade());
      return processMap;
    }

    public static class upgrade<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, upgrade_args, User> {
      public upgrade() {
        super("upgrade");
      }

      public upgrade_args getEmptyArgsInstance() {
        return new upgrade_args();
      }

      public AsyncMethodCallback<User> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
        final org.apache.thrift.AsyncProcessFunction fcall = this;
        return new AsyncMethodCallback<User>() { 
          public void onComplete(User o) {
            upgrade_result result = new upgrade_result();
            result.success = o;
            try {
              fcall.sendResponse(fb,result, org.apache.thrift.protocol.TMessageType.REPLY,seqid);
              return;
            } catch (Exception e) {
              LOGGER.error("Exception writing to internal frame buffer", e);
            }
            fb.close();
          }
          public void onError(Exception e) {
            byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
            org.apache.thrift.TBase msg;
            upgrade_result result = new upgrade_result();
            if (e instanceof TZException) {
                        result.ex = (TZException) e;
                        result.setExIsSet(true);
                        msg = result;
            }
             else 
            {
              msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
              msg = (org.apache.thrift.TBase)new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
            }
            try {
              fcall.sendResponse(fb,msg,msgType,seqid);
              return;
            } catch (Exception ex) {
              LOGGER.error("Exception writing to internal frame buffer", ex);
            }
            fb.close();
          }
        };
      }

      protected boolean isOneway() {
        return false;
      }

      public void start(I iface, upgrade_args args, org.apache.thrift.async.AsyncMethodCallback<User> resultHandler) throws TException {
        iface.upgrade(args.user,resultHandler);
      }
    }

  }

  public static class upgrade_args implements org.apache.thrift.TBase<upgrade_args, upgrade_args._Fields>, java.io.Serializable, Cloneable, Comparable<upgrade_args>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("upgrade_args");

    private static final org.apache.thrift.protocol.TField USER_FIELD_DESC = new org.apache.thrift.protocol.TField("user", org.apache.thrift.protocol.TType.STRUCT, (short)1);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new upgrade_argsStandardSchemeFactory());
      schemes.put(TupleScheme.class, new upgrade_argsTupleSchemeFactory());
      schemes.put(TupleBinaryScheme.class, new upgrade_argsTupleBinarySchemeFactory());
    }

    public User user; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      USER((short)1, "user");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 1: // USER
            return USER;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.USER, new org.apache.thrift.meta_data.FieldMetaData("user", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, User.class)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(upgrade_args.class, metaDataMap);
    }

    public upgrade_args() {
    }

    public upgrade_args(
      User user)
    {
      this();
      this.user = user;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public upgrade_args(upgrade_args other) {
      if (other.isSetUser()) {
        this.user = new User(other.user);
      }
    }

    public upgrade_args deepCopy() {
      return new upgrade_args(this);
    }

    @Override
    public void clear() {
      this.user = null;
    }

    public User getUser() {
      return this.user;
    }

    public upgrade_args setUser(User user) {
      this.user = user;
      return this;
    }

    public void unsetUser() {
      this.user = null;
    }

    /** Returns true if field user is set (has been assigned a value) and false otherwise */
    public boolean isSetUser() {
      return this.user != null;
    }

    public void setUserIsSet(boolean value) {
      if (!value) {
        this.user = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case USER:
        if (value == null) {
          unsetUser();
        } else {
          setUser((User)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case USER:
        return getUser();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case USER:
        return isSetUser();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof upgrade_args)
        return this.equals((upgrade_args)that);
      return false;
    }

    public boolean equals(upgrade_args that) {
      if (that == null)
        return false;

      boolean this_present_user = true && this.isSetUser();
      boolean that_present_user = true && that.isSetUser();
      if (this_present_user || that_present_user) {
        if (!(this_present_user && that_present_user))
          return false;
        if (!this.user.equals(that.user))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public int compareTo(upgrade_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = Boolean.valueOf(isSetUser()).compareTo(other.isSetUser());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetUser()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.user, other.user);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("upgrade_args(");
      boolean first = true;

      sb.append("user:");
      if (this.user == null) {
        sb.append("null");
      } else {
        sb.append(this.user);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
      if (user != null) {
        user.validate();
      }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class upgrade_argsStandardSchemeFactory implements SchemeFactory {
      public upgrade_argsStandardScheme getScheme() {
        return new upgrade_argsStandardScheme();
      }
    }

    private static class upgrade_argsStandardScheme extends StandardScheme<upgrade_args> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, upgrade_args struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 1: // USER
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.user = new User();
                struct.user.read(iprot);
                struct.setUserIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, upgrade_args struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.user != null) {
          oprot.writeFieldBegin(USER_FIELD_DESC);
          struct.user.write(oprot);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class upgrade_argsTupleSchemeFactory implements SchemeFactory {
      public upgrade_argsTupleScheme getScheme() {
        return new upgrade_argsTupleScheme();
      }
    }

    private static class upgrade_argsTupleScheme extends TupleScheme<upgrade_args> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, upgrade_args struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetUser()) {
          optionals.set(0);
        }
        oprot.writeBitSet(optionals, 1);
        if (struct.isSetUser()) {
          struct.user.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, upgrade_args struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
        BitSet incoming = iprot.readBitSet(1);
        if (incoming.get(0)) {
          struct.user = new User();
          struct.user.read(iprot);
          struct.setUserIsSet(true);
        }
      }
    }

    private static class upgrade_argsTupleBinarySchemeFactory implements SchemeFactory {
      public upgrade_argsTupleBinaryScheme getScheme() {
        return new upgrade_argsTupleBinaryScheme();
      }
    }

    private static class upgrade_argsTupleBinaryScheme extends TupleBinaryScheme<upgrade_args> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, upgrade_args struct) throws org.apache.thrift.TException {
        TTupleBinaryProtocol oprot = (TTupleBinaryProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetUser()) {
          optionals.set(0);
        }
        oprot.writeBitSet(optionals, 1);
        if (struct.isSetUser()) {
          struct.user.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, upgrade_args struct) throws org.apache.thrift.TException {
        TTupleBinaryProtocol iprot = (TTupleBinaryProtocol) prot;
        BitSet incoming = iprot.readBitSet(1);
        if (incoming.get(0)) {
          struct.user = new User();
          struct.user.read(iprot);
          struct.setUserIsSet(true);
        }
      }
    }

  }

  public static class upgrade_result implements org.apache.thrift.TBase<upgrade_result, upgrade_result._Fields>, java.io.Serializable, Cloneable, Comparable<upgrade_result>   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("upgrade_result");

    private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRUCT, (short)0);
    private static final org.apache.thrift.protocol.TField EX_FIELD_DESC = new org.apache.thrift.protocol.TField("ex", org.apache.thrift.protocol.TType.STRUCT, (short)1);

    private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
    static {
      schemes.put(StandardScheme.class, new upgrade_resultStandardSchemeFactory());
      schemes.put(TupleScheme.class, new upgrade_resultTupleSchemeFactory());
      schemes.put(TupleBinaryScheme.class, new upgrade_resultTupleBinarySchemeFactory());
    }

    public User success; // required
    public TZException ex; // required

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      SUCCESS((short)0, "success"),
      EX((short)1, "ex");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 0: // SUCCESS
            return SUCCESS;
          case 1: // EX
            return EX;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments
    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, User.class)));
      tmpMap.put(_Fields.EX, new org.apache.thrift.meta_data.FieldMetaData("ex", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(upgrade_result.class, metaDataMap);
    }

    public upgrade_result() {
    }

    public upgrade_result(
      User success,
      TZException ex)
    {
      this();
      this.success = success;
      this.ex = ex;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public upgrade_result(upgrade_result other) {
      if (other.isSetSuccess()) {
        this.success = new User(other.success);
      }
      if (other.isSetEx()) {
        this.ex = new TZException(other.ex);
      }
    }

    public upgrade_result deepCopy() {
      return new upgrade_result(this);
    }

    @Override
    public void clear() {
      this.success = null;
      this.ex = null;
    }

    public User getSuccess() {
      return this.success;
    }

    public upgrade_result setSuccess(User success) {
      this.success = success;
      return this;
    }

    public void unsetSuccess() {
      this.success = null;
    }

    /** Returns true if field success is set (has been assigned a value) and false otherwise */
    public boolean isSetSuccess() {
      return this.success != null;
    }

    public void setSuccessIsSet(boolean value) {
      if (!value) {
        this.success = null;
      }
    }

    public TZException getEx() {
      return this.ex;
    }

    public upgrade_result setEx(TZException ex) {
      this.ex = ex;
      return this;
    }

    public void unsetEx() {
      this.ex = null;
    }

    /** Returns true if field ex is set (has been assigned a value) and false otherwise */
    public boolean isSetEx() {
      return this.ex != null;
    }

    public void setExIsSet(boolean value) {
      if (!value) {
        this.ex = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case SUCCESS:
        if (value == null) {
          unsetSuccess();
        } else {
          setSuccess((User)value);
        }
        break;

      case EX:
        if (value == null) {
          unsetEx();
        } else {
          setEx((TZException)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case SUCCESS:
        return getSuccess();

      case EX:
        return getEx();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case SUCCESS:
        return isSetSuccess();
      case EX:
        return isSetEx();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof upgrade_result)
        return this.equals((upgrade_result)that);
      return false;
    }

    public boolean equals(upgrade_result that) {
      if (that == null)
        return false;

      boolean this_present_success = true && this.isSetSuccess();
      boolean that_present_success = true && that.isSetSuccess();
      if (this_present_success || that_present_success) {
        if (!(this_present_success && that_present_success))
          return false;
        if (!this.success.equals(that.success))
          return false;
      }

      boolean this_present_ex = true && this.isSetEx();
      boolean that_present_ex = true && that.isSetEx();
      if (this_present_ex || that_present_ex) {
        if (!(this_present_ex && that_present_ex))
          return false;
        if (!this.ex.equals(that.ex))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    @Override
    public int compareTo(upgrade_result other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;

      lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetSuccess()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      lastComparison = Boolean.valueOf(isSetEx()).compareTo(other.isSetEx());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetEx()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ex, other.ex);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
      }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("upgrade_result(");
      boolean first = true;

      sb.append("success:");
      if (this.success == null) {
        sb.append("null");
      } else {
        sb.append(this.success);
      }
      first = false;
      if (!first) sb.append(", ");
      sb.append("ex:");
      if (this.ex == null) {
        sb.append("null");
      } else {
        sb.append(this.ex);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
      // check for sub-struct validity
      if (success != null) {
        success.validate();
      }
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private static class upgrade_resultStandardSchemeFactory implements SchemeFactory {
      public upgrade_resultStandardScheme getScheme() {
        return new upgrade_resultStandardScheme();
      }
    }

    private static class upgrade_resultStandardScheme extends StandardScheme<upgrade_result> {

      public void read(org.apache.thrift.protocol.TProtocol iprot, upgrade_result struct) throws org.apache.thrift.TException {
        org.apache.thrift.protocol.TField schemeField;
        iprot.readStructBegin();
        while (true)
        {
          schemeField = iprot.readFieldBegin();
          if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
            break;
          }
          switch (schemeField.id) {
            case 0: // SUCCESS
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.success = new User();
                struct.success.read(iprot);
                struct.setSuccessIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            case 1: // EX
              if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
                struct.ex = new TZException();
                struct.ex.read(iprot);
                struct.setExIsSet(true);
              } else { 
                org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
              }
              break;
            default:
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
          }
          iprot.readFieldEnd();
        }
        iprot.readStructEnd();

        // check for required fields of primitive type, which can't be checked in the validate method
        struct.validate();
      }

      public void write(org.apache.thrift.protocol.TProtocol oprot, upgrade_result struct) throws org.apache.thrift.TException {
        struct.validate();

        oprot.writeStructBegin(STRUCT_DESC);
        if (struct.success != null) {
          oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
          struct.success.write(oprot);
          oprot.writeFieldEnd();
        }
        if (struct.ex != null) {
          oprot.writeFieldBegin(EX_FIELD_DESC);
          struct.ex.write(oprot);
          oprot.writeFieldEnd();
        }
        oprot.writeFieldStop();
        oprot.writeStructEnd();
      }

    }

    private static class upgrade_resultTupleSchemeFactory implements SchemeFactory {
      public upgrade_resultTupleScheme getScheme() {
        return new upgrade_resultTupleScheme();
      }
    }

    private static class upgrade_resultTupleScheme extends TupleScheme<upgrade_result> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, upgrade_result struct) throws org.apache.thrift.TException {
        TTupleProtocol oprot = (TTupleProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetSuccess()) {
          optionals.set(0);
        }
        if (struct.isSetEx()) {
          optionals.set(1);
        }
        oprot.writeBitSet(optionals, 2);
        if (struct.isSetSuccess()) {
          struct.success.write(oprot);
        }
        if (struct.isSetEx()) {
          struct.ex.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, upgrade_result struct) throws org.apache.thrift.TException {
        TTupleProtocol iprot = (TTupleProtocol) prot;
        BitSet incoming = iprot.readBitSet(2);
        if (incoming.get(0)) {
          struct.success = new User();
          struct.success.read(iprot);
          struct.setSuccessIsSet(true);
        }
        if (incoming.get(1)) {
          struct.ex = new TZException();
          struct.ex.read(iprot);
          struct.setExIsSet(true);
        }
      }
    }

    private static class upgrade_resultTupleBinarySchemeFactory implements SchemeFactory {
      public upgrade_resultTupleBinaryScheme getScheme() {
        return new upgrade_resultTupleBinaryScheme();
      }
    }

    private static class upgrade_resultTupleBinaryScheme extends TupleBinaryScheme<upgrade_result> {

      @Override
      public void write(org.apache.thrift.protocol.TProtocol prot, upgrade_result struct) throws org.apache.thrift.TException {
        TTupleBinaryProtocol oprot = (TTupleBinaryProtocol) prot;
        BitSet optionals = new BitSet();
        if (struct.isSetSuccess()) {
          optionals.set(0);
        }
        if (struct.isSetEx()) {
          optionals.set(1);
        }
        oprot.writeBitSet(optionals, 2);
        if (struct.isSetSuccess()) {
          struct.success.write(oprot);
        }
        if (struct.isSetEx()) {
          struct.ex.write(oprot);
        }
      }

      @Override
      public void read(org.apache.thrift.protocol.TProtocol prot, upgrade_result struct) throws org.apache.thrift.TException {
        TTupleBinaryProtocol iprot = (TTupleBinaryProtocol) prot;
        BitSet incoming = iprot.readBitSet(2);
        if (incoming.get(0)) {
          struct.success = new User();
          struct.success.read(iprot);
          struct.setSuccessIsSet(true);
        }
        if (incoming.get(1)) {
          struct.ex = new TZException();
          struct.ex.read(iprot);
          struct.setExIsSet(true);
        }
      }
    }

  }

}

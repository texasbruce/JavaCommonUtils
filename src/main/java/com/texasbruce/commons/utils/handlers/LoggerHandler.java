package com.texasbruce.commons.utils.handlers;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

public class LoggerHandler implements SOAPHandler<SOAPMessageContext>{

    private final static Logger DEFAULT_LOGGER = Logger.getLogger(LoggerHandler.class);

    private Logger logger = null;
    
    public LoggerHandler() {
        super();
        this.logger = DEFAULT_LOGGER;
    }
    
    public LoggerHandler (Logger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public boolean handleFault(javax.xml.ws.handler.soap.SOAPMessageContext arg0) {
        try {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            arg0.getMessage().writeTo(os);
            logger.error((Boolean.TRUE.equals((Boolean)arg0.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)) ? "Outbound" : "Inbound")
                    + " SOAPFault - " + os.toString());
        } catch (Exception e) {
            logger.error("", e);
        }
        return true;
    }

    @Override
    public boolean handleMessage(javax.xml.ws.handler.soap.SOAPMessageContext arg0) {
        try {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            arg0.getMessage().writeTo(os);
            logger.debug((Boolean.TRUE.equals((Boolean)arg0.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)) ? "Outbound" : "Inbound")
                    + " SOAPMessage - " + os.toString());
        } catch (Exception e) {
            logger.error("", e);
        }
        
        return true;
    }

    @Override
    public void close(MessageContext context) {
        
    }

    @Override
    public java.util.Set<QName> getHeaders() {
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public static void addLoggerHandlerChain (javax.xml.ws.BindingProvider bp) {
        java.util.List<javax.xml.ws.handler.Handler> handlers = new java.util.ArrayList<javax.xml.ws.handler.Handler>();
        java.util.List<javax.xml.ws.handler.Handler> existingHandlers = bp.getBinding().getHandlerChain();
        
        if (existingHandlers != null) {
            handlers.addAll(existingHandlers);
        }
        
        handlers.add(new LoggerHandler());
        bp.getBinding().setHandlerChain(handlers);

    }
    
    @SuppressWarnings("rawtypes")
    public static void addLoggerHandlerChain (javax.xml.ws.BindingProvider bp, Logger logger) {
        java.util.List<javax.xml.ws.handler.Handler> handlers = new java.util.ArrayList<javax.xml.ws.handler.Handler>();
        java.util.List<javax.xml.ws.handler.Handler> existingHandlers = bp.getBinding().getHandlerChain();
        
        if (existingHandlers != null) {
            handlers.addAll(existingHandlers);
        }
        
        handlers.add(new LoggerHandler(logger));
        bp.getBinding().setHandlerChain(handlers);
    }

}

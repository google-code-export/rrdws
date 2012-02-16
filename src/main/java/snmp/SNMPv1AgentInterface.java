/*
 * SNMP Package
 *
 * Copyright (C) 2002, Jonathan Sevy <jsevy@mcs.drexel.edu>
 *
 * This is free software. Redistribution and use in source and binary forms, with
 * or without modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice, this
 *     list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation 
 *     and/or other materials provided with the distribution.
 *  3. The name of the author may not be used to endorse or promote products 
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED 
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */


package snmp;

import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;


/**
*	The class SNMPv1AgentInterface implements an interface for responding to requests sent from a remote SNMP 
*   manager. The agent simply listens for requests for information, and passes requested OIDs on to concrete 
*   subclasses of SNMPRequestListener. These are expected to retrieve requested information from the system,
*   and return this to the agent interface for inclusion in a response to the manager. 
*   The approach is that from version 1 of SNMP, using no encryption of data. Communication occurs
*	via UDP, using port 162, the standard SNMP trap port, as the destination port.
*/

public class SNMPv1AgentInterface
					implements Runnable
{
	public static final int SNMP_PORT = 161;
	
	// largest size for datagram packet payload; based on
	// RFC 1157, need to handle messages of at least 484 bytes
	public static final int MAXSIZE = 512;
	
	int version = 0;
	
	private DatagramSocket dSocket;
	private Thread receiveThread;
	private Vector listenerVector;
	
	
	
	/**
	*	Construct a new agent object to listen for requests from remote SNMP managers. The agent listens
	*   on the standard SNMP UDP port 161.
	*/
	
	public SNMPv1AgentInterface(int version)
		throws SocketException
	{
		this(version, SNMP_PORT);
	}
	

	
	/**
	*	Construct a new agent object to listen for requests from remote SNMP managers. The agent listens
	*   on the supplied port.
	*/
	
	public SNMPv1AgentInterface(int version, int localPort)
		throws SocketException
	{
		
		this.version = version;
		
		dSocket = new DatagramSocket(localPort);
		
		listenerVector = new Vector();
			
		receiveThread = new Thread(this);
		
	}
	
	
	
	public void addRequestListener(SNMPRequestListener listener)
	{
		// see if listener already added; if so, ignore
		for (int i = 0; i < listenerVector.size(); i++)
		{
			if (listener == listenerVector.elementAt(i))
			{
				return;
			}
		}
		
		// if got here, it's not in the list; add it
		listenerVector.add(listener);
	}
	
	
	
	public void removeRequestListener(SNMPRequestListener listener)
	{
		// see if listener in list; if so, remove, if not, ignore
		for (int i = 0; i < listenerVector.size(); i++)
		{
			if (listener == listenerVector.elementAt(i))
			{
				listenerVector.removeElementAt(i);
				break;
			}
		}
		
	}

	
	
	/**
	*	Start listening for requests from remote managers.
	*/
	
	public void startReceiving()
	{
		// if receiveThread not already running, start it
		if (!receiveThread.isAlive())
		{
			receiveThread = new Thread(this);
			receiveThread.start();
		}
	}
	

	
	
	/**
	*	Stop listening for requests from remote managers.
	*/
	
	public void stopReceiving()
		throws SocketException
	{
		// interrupt receive thread so it will die a natural death
		receiveThread.interrupt();
	}

	
	
	
	
	/**
	*	The run() method for the agent interface's listener. Just waits for SNMP request messages to
	*	come in on port 161 (or the port supplied in the constructor), then dispatches the retrieved 
	*   SNMPPDU and community name to each of the registered SNMPRequestListeners by calling their 
	*   processRequest() methods.
	*/
	
	public void run()
	{
		
		
		try
		{
		
			while (!receiveThread.isInterrupted())
			{
				
				DatagramPacket inPacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE);
		
				dSocket.receive(inPacket);
				
				InetAddress requesterAddress = inPacket.getAddress();
				int requesterPort = inPacket.getPort();
				
				byte[] encodedMessage = inPacket.getData();
				
				/*
				System.out.println("Message bytes length (in): " + inPacket.getLength());
				
				System.out.println("Message bytes (in):");
				for (int i = 0; i < encodedMessage.length; ++i)
				{
					System.out.print(hexByte(encodedMessage[i]) + " ");
				}
				System.out.println("\n");
				*/
				
				SNMPMessage receivedMessage = new SNMPMessage(SNMPBERCodec.extractNextTLV(encodedMessage,0).value);
				
				String communityName = receivedMessage.getCommunityName();
				SNMPPDU receivedPDU = receivedMessage.getPDU();
				byte requestPDUType = receivedPDU.getPDUType();
				
				//System.out.println("Received message; community = " + communityName + ", pdu type = " + Byte.toString(requestPDUType));
				//System.out.println("  read community = " + readCommunityName + ", write community = " + writeCommunityName);
				
				
	            SNMPSequence requestedVarList = receivedPDU.getVarBindList();
    			
				Hashtable variablePairHashtable = new Hashtable();
				SNMPSequence responseVarList = new SNMPSequence();
    			int errorIndex = 0;
    			int errorStatus = SNMPRequestException.NO_ERROR;
				int requestID = receivedPDU.getRequestID();
				
				try
				{
				
    				// pass the received PDU and community name to the processRequest method of any listeners;
    				// handle differently depending on whether the request is a get-next, or a get or set
				
				    if ((requestPDUType == SNMPBERCodec.SNMPGETREQUEST) || (requestPDUType == SNMPBERCodec.SNMPSETREQUEST))
				    {
				
				        // pass the received PDU and community name to any registered listeners
    				    for (int i = 0; i < listenerVector.size(); i++)
        				{
        					SNMPRequestListener listener = (SNMPRequestListener)listenerVector.elementAt(i);
        					
        					// return value is sequence of variable pairs for those OIDs handled by the listener
        					SNMPSequence handledVarList = listener.processRequest(receivedPDU, communityName);
           					
           					// add to Hashtable of handled OIDs, if not already there
           					for (int j = 0; j < handledVarList.size(); j++)
           					{
           					    
           					    SNMPSequence handledPair = (SNMPSequence)handledVarList.getSNMPObjectAt(j);
           					    SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)handledPair.getSNMPObjectAt(0);
                       		    SNMPObject snmpObject = (SNMPObject)handledPair.getSNMPObjectAt(1);
                       		    
                       		    if (!variablePairHashtable.containsKey(snmpOID))
                       		    {
                       		        variablePairHashtable.put(snmpOID, snmpObject);
                       		    }
                       		    
           					}
        					
        				}
        				
        				
        				
        				// construct response containing the handled OIDs; if any OID not handled, throw exception
        				for (int j = 0; j < requestedVarList.size(); j++)
        				{
        				    SNMPSequence requestPair = (SNMPSequence)requestedVarList.getSNMPObjectAt(j);
      					    SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)requestPair.getSNMPObjectAt(0);
                  		    
                  		    // find corresponding SNMP object in hashtable
                  		    if (!variablePairHashtable.containsKey(snmpOID))
                  		    {
                  		        errorIndex = j + 1;
                  		        errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
                  		        
                  		        if (requestPDUType == SNMPBERCodec.SNMPGETREQUEST)
                  		            throw new SNMPGetException("OID " + snmpOID + " not handled", errorIndex, errorStatus);
                  		        else
                  		            throw new SNMPSetException("OID " + snmpOID + " not handled", errorIndex, errorStatus);
                  		    }
                  		    
                  		    SNMPObject snmpObject = (SNMPObject)variablePairHashtable.get(snmpOID);
                  		    SNMPVariablePair responsePair = new SNMPVariablePair(snmpOID, snmpObject);
                  		    
                  		    responseVarList.addSNMPObject(responsePair);
    		                
        				}
    				
    				}
    				else if (requestPDUType == SNMPBERCodec.SNMPGETNEXTREQUEST)
    				{
    				    // pass the received PDU and community name to any registered listeners
    				    for (int i = 0; i < listenerVector.size(); i++)
        				{
        					SNMPRequestListener listener = (SNMPRequestListener)listenerVector.elementAt(i);
        					
        					// return value is sequence of nested variable pairs for those OIDs handled by the listener:
        					// consists of (supplied OID, (following OID, value)) nested variable pairs
        					SNMPSequence handledVarList = listener.processGetNextRequest(receivedPDU, communityName);
           					
           					// add variable pair to Hashtable of handled OIDs, if not already there
           					for (int j = 0; j < handledVarList.size(); j++)
           					{
           					    
           					    SNMPSequence handledPair = (SNMPSequence)handledVarList.getSNMPObjectAt(j);
           					    SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)handledPair.getSNMPObjectAt(0);
                       		    SNMPObject snmpObject = (SNMPObject)handledPair.getSNMPObjectAt(1);
                       		    
                       		    if (!variablePairHashtable.containsKey(snmpOID))
                       		    {
                       		        variablePairHashtable.put(snmpOID, snmpObject);
                       		    }
                       		    
           					}
        					
        				}
        				
        				
        				
        				// construct response containing the handled OIDs; if any OID not handled, throw exception
        				for (int j = 0; j < requestedVarList.size(); j++)
        				{
        				    SNMPSequence requestPair = (SNMPSequence)requestedVarList.getSNMPObjectAt(j);
      					    SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)requestPair.getSNMPObjectAt(0);
                  		    
                  		    // find corresponding SNMP object in hashtable
                  		    if (!variablePairHashtable.containsKey(snmpOID))
                  		    {
                  		        errorIndex = j + 1;
                  		        errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
                  		        
                  		        throw new SNMPGetException("OID " + snmpOID + " not handled", errorIndex, errorStatus);
                   		    }
                  		    
                  		    // value in hashtable is complete variable pair
                  		    SNMPVariablePair responsePair = (SNMPVariablePair)variablePairHashtable.get(snmpOID);
                  		    
                  		    responseVarList.addSNMPObject(responsePair);
    		                
        				}
        				
    				}
    				else
    				{
    				    // some other PDU type; silently ignore
    				    continue;
    				}
    				
    				
				}
				catch (SNMPRequestException e)
				{
				    // exception should contain the index and cause of error; return this in message
				    errorIndex = e.errorIndex;
				    errorStatus = e.errorStatus;
				    
				    // just return request variable list as response variable list
				    responseVarList = requestedVarList;
				}
				catch (Exception e)
        		{
        			// don't have a specific index and cause of error; return message as general error, index 0
				    errorIndex = 0;
				    errorStatus = SNMPRequestException.FAILED;
				    
				    // just return request variable list as response variable list
				    responseVarList = requestedVarList;
				    
				    // also report the exception locally
				    System.out.println("Exception while processing request: " + e.toString());
        		}
				
				
				SNMPPDU pdu = new SNMPPDU(SNMPBERCodec.SNMPGETRESPONSE, requestID, errorStatus, errorIndex, responseVarList);
		        SNMPMessage message = new SNMPMessage(version, communityName, pdu);
		        byte[] messageEncoding = message.getBEREncoding();
		        DatagramPacket outPacket = new DatagramPacket(messageEncoding, messageEncoding.length, requesterAddress, requesterPort);
		
		
		        dSocket.send(outPacket);
				
				
			}
		
		}
		catch (IOException e)
		{
			// just report the problem
			System.out.println("IOException during request processing: " + e.toString());
		}
		catch (SNMPBadValueException e)
		{
			// just report the problem
			System.out.println("SNMPBadValueException during request processing: " + e.toString());
		}
		catch (Exception e)
		{
			// just report the problem
			System.out.println("Exception during request processing: " + e.toString());
		}
				
	}
	
	
	
	
	private String hexByte(byte b)
	{
		int pos = b;
		if (pos < 0)
			pos += 256;
		String returnString = new String();
		returnString += Integer.toHexString(pos/16);
		returnString += Integer.toHexString(pos%16);
		return returnString;
	}
	
	
	
}




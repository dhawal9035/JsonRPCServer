package edu.asu.msse.dssoni;

import java.net.*;
import java.io.*;

//import ser423.student.server.MovieCollectionImpl;

public class MovieServer extends Thread {
	private Socket conn;
	   private int id;
	   MovieCollection movieLib;
	   MovieCollectionSkeleton skeleton;

	   public MovieServer (Socket sock, int id, MovieCollection movieLib) {
	      this.conn = sock;
	      this.id = id;
	      this.movieLib = movieLib;
	      skeleton = new MovieCollectionSkeleton(movieLib);
	   }

	   public void run() {
	      try {
	         OutputStream outSock = conn.getOutputStream();
	         InputStream inSock = conn.getInputStream();
	         byte clientInput[] = new byte[4096]; // up to 1024 bytes in a message.
	         int numr = inSock.read(clientInput,0,4096);
	         if (numr != -1) {
	            //System.out.println("read "+numr+" bytes. Available: "+
	            //                   inSock.available());
	            Thread.sleep(200);
	            int ind = numr;
	            while(inSock.available()>0){
	               numr = inSock.read(clientInput,ind,4096-ind);
	               ind = numr + ind;
	               Thread.sleep(200);
	            }
	            String clientString = new String(clientInput,0,ind);
	            //System.out.println("read from client: "+id+" the string: "
	            //                   +clientString);
	            if(clientString.indexOf("{")>=0){
	               String request = clientString.substring(clientString.indexOf("{"));
	               //System.out.println("request from client: "+request);
	               String response = skeleton.callMethod(request);
	               byte clientOut[] = response.getBytes();
	               outSock.write(clientOut,0,clientOut.length);
	              //System.out.println("response is: "+response);
	            }else{
	               System.out.println("No json object in clientString: "+
	                                  clientString);
	            }
	         }
	         inSock.close();
	         outSock.close();
	         conn.close();
	      } catch (Exception e) {
	         System.out.println("Can't get I/O for the connection.");
	      }
	   }

	   public static void main (String args[]) {
	      Socket sock;
	      MovieCollection mLib;
	      int id=0;
	      int portNo = 8080;
	      try {
	         if (args.length < 1) {
	            System.out.println("Usage: java -jar lib/server.jar portNum");
	            System.exit(0);
	         }
	         portNo = Integer.parseInt(args[0]);
	         mLib = new MovieCollectionImpl();
	         if (portNo <= 1024) portNo=8080;
	         ServerSocket serv = new ServerSocket(portNo);
	         while (true) {
	            System.out.println("Student server waiting for connects on port "
	                               +portNo);
	            sock = serv.accept();
	            System.out.println("Student server connected to client: "+id);
	            MovieServer myServerThread = new MovieServer(sock,id++,mLib);
	            myServerThread.start();
	         }
	      } catch(Exception e) {e.printStackTrace();}
	   }
}

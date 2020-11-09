<!doctype html public "-//w3c/dtd HTML 4.0//en">
<%@ page import="java.util.TreeSet" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.net.InetAddress" %>
<%@ page import="java.net.UnknownHostException" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.*" %>
<html>
<head>
<title>Snoop</title>
</head>

<body bgcolor="#FFFFFF">
<font face="Helvetica">

<h2>
<font color=#DB1260>
Snooping... (Hostname: <%= getLocalHostName()%>)
</font>
</h2>
<hr />
<%
	session = request.getSession();
        java.util.Properties p = new java.util.Properties ();
        System.out.println("sto per caricare");
        try{
        p.load(application.getResourceAsStream("/WEB-INF/classes/application.properties"));
        System.out.println("caricato...");
        }catch(NullPointerException npe){
           %><h3>Attenzione: problemi nel raggiungere application.properties</h3><%
        }
        java.util.Enumeration en = p.propertyNames();
        String propName = null;
%>
<p>
<center>
<table border=1 cellspacing=2 cellpadding=5 width=400 bgcolor=#EEEEEE>
<th colspan=2>Version info<br />

</th>
<tr>
<td><B>Name</B></td>
<td><B>Value</B></td>
</tr>

<%
  while (en.hasMoreElements()) {
      propName = (String) en.nextElement();
%>

<tr>
<td><%= propName %></td>
<td><%= p.getProperty( propName ) %></td>
</tr>

<%
  } // end of while loop for session names
%>

</table>
</center>
<hr />
<p>
HttpSession is: <%=session%>
</p>

<p>
HttpSession ID is: <%=session.getId()%>
</p>
<hr />
<%
  if (request.getParameter("AddValue") != null) {
    session.setAttribute(request.getParameter("NameField"),
  	              request.getParameter("ValueField"));
  } else if (request.getParameter("DeleteValue") != null) {
    session.removeAttribute(request.getParameter("NameField"));
  }
%>
<center>
<p>

<table border=1 cellspacing=2 cellpadding=5 width=400 bgcolor=#EEEEEE align="center">
    <th colspan=2>Session<br />

</th>
<tr>
<td><B>Name</B></td>
<td><B>Value</B></td>
</tr>

<%
  java.util.Enumeration sessionNames = session.getAttributeNames();
  String name=null;
  while (sessionNames.hasMoreElements()) {
    name = (String)sessionNames.nextElement();
%>

<tr>
<td><%= name %></td>
<td><%= session.getAttribute(name).toString() %></td>
</tr>

<%
  } // end of while loop for session names
%>

</table>
  <hr />
</center>

<center>
  <table border=1 cellspacing=2 cellpadding=5 width=300 bgcolor=#EEEEEE align="center">
    <th colspan=3>Application attributes<br />

</th>
<tr>
<td><B>Name</B></td>
<td><B>Value</B></td>
<td><B>Type</B></td>
</tr>

<%
  java.util.Enumeration applicationNames = application.getAttributeNames();
  String name1= null;
  String className = null;
  while (applicationNames.hasMoreElements()) {
    name1 = (String)applicationNames.nextElement();
    Object o = application.getAttribute(name1);
    className = o.getClass().getName();
%>

<tr>
<td><%=name1 %></td>
<td><%=application.getAttribute(name1).toString() %></td>
<td><%=className %></td>
</tr>

<%
  } // end of while loop for session names
%>

</table>
</center>
<%!
	String allinfo = new String();
	static StringBuffer allinfo_sb = new StringBuffer();
%>
<pre>


<b>Current Date</b>
<%= (new Date()).toString() %>

<%
	allinfo_sb.append((new Date()).toString());
%>

<b>Request Info</b>
Protocol:                        <%= ""+request.getProtocol() %>
ServerName:                      <%= ""+request.getServerName() %>
ServerPort:                      <%= ""+request.getServerPort() %>
Secure:                          <%= ""+request.isSecure() %>
ContextPath:                     <%= ""+request.getContextPath() %>
ServletPath:                     <%= ""+request.getServletPath() %>
QueryString:                     <%= ""+request.getQueryString() %>
PathInfo:                        <%= ""+request.getPathInfo() %>
PathTranslated:                  <%= ""+request.getPathTranslated() %>
RequestURI:                      <%= ""+request.getRequestURI() %>
AuthType:                        <%= ""+request.getAuthType() %>
ContentType:                     <%= ""+request.getContentType() %>
Locale:                          <%= ""+request.getLocale() %>
Method:                          <%= ""+request.getMethod() %>
Session:                         <%= ""+request.getSession() %>
RequestedSessionId:              <%= ""+request.getRequestedSessionId() %>
RequestedSessionIdFromCookie:    <%= ""+request.isRequestedSessionIdFromCookie() %>
RequestedSessionIdFromURL:       <%= ""+request.isRequestedSessionIdFromURL() %>
RemoteUser:                      <%= ""+request.getRemoteUser() %>
RemoteAddr:                      <%= ""+request.getRemoteAddr() %>
RemoteHost:                      <%= ""+request.getRemoteHost() %>
<%
	allinfo_sb.append("Protocol:\t\t\t\t" + request.getProtocol() + "\n");
	allinfo_sb.append("ServerName:\t\t\t\t"+ request.getServerName() + "\n");
	allinfo_sb.append("ServerPort:\t\t\t\t"+request.getServerPort() + "\n");
	allinfo_sb.append("Secure:\t\t\t\t"+request.isSecure() + "\n");
	allinfo_sb.append("ContextPath:\t\t\t\t"+request.getContextPath() + "\n");
	allinfo_sb.append("ServletPath:\t\t\t\t"+request.getServletPath() + "\n");
	allinfo_sb.append("QueryString:\t\t\t\t"+request.getQueryString() + "\n");
	allinfo_sb.append("PathInfo:\t\t\t\t"+request.getPathInfo() + "\n");
	allinfo_sb.append("PathTranslated:\t\t\t\t"+request.getPathTranslated() + "\n");
	allinfo_sb.append("RequestURI:\t\t\t\t"+request.getRequestURI() + "\n");
	allinfo_sb.append("AuthType:\t\t\t\t"+request.getAuthType() + "\n");
	allinfo_sb.append("ContentType:\t\t\t\t"+request.getContentType() + "\n");
	allinfo_sb.append("Locale:\t\t\t\t"+request.getLocale() + "\n");
	allinfo_sb.append("Method:\t\t\t\t"+request.getMethod() + "\n");
	allinfo_sb.append("Session:\t\t\t\t"+request.getSession() + "\n");
	allinfo_sb.append("RequestedSessionId:\t\t\t\t"+request.getRequestedSessionId() + "\n");
	allinfo_sb.append("RequestedSessionIdFromCookie:\t\t\t\t"+request.isRequestedSessionIdFromCookie() + "\n");
	allinfo_sb.append("RequestedSessionIdFromURL:\t\t\t\t"+request.isRequestedSessionIdFromURL() + "\n");
	allinfo_sb.append("UserPrincipal:\t\t\t\t"+request.getUserPrincipal() + "\n");
	allinfo_sb.append("RemoteUser:\t\t\t\t"+request.getRemoteUser() + "\n");
	allinfo_sb.append("RemoteAddr:\t\t\t\t"+request.getRemoteAddr() + "\n");
	allinfo_sb.append("RemoteHost:\t\t\t\t"+request.getRemoteHost() + "\n");
%>

<b>Parameters</b>
<%
  {
    Collection sortem = new TreeSet();
    int longest = getLongest(request.getParameterNames());
    Enumeration parameters = request.getParameterNames();
    while(parameters.hasMoreElements()) {
      String param = (String)parameters.nextElement();
      String[] values = request.getParameterValues(param);
      for(int i=0; i<values.length; i++) {
        sortem.add(padStringWidth(param,longest)+" = "+values[i]);
      }
    }
    print(sortem,out);
  }
%>
<b>Attributes</b>
<%
  {
    Collection sortem = new TreeSet();
    int longestName = getLongest(request.getAttributeNames());
    Enumeration attributes = request.getAttributeNames();
    while(attributes.hasMoreElements()) {
      name = (String)attributes.nextElement();
      Object value =  request.getAttribute(name);
      sortem.add(padStringWidth(name,longestName)+" = "+value);
    }
    print(sortem,out);
  }
%>
<b>Headers</b>
<%
  {
    Collection sortem = new TreeSet();
    int longest = getLongest(request.getHeaderNames());
    Enumeration headers = request.getHeaderNames();
    while(headers.hasMoreElements()) {
      name = (String)headers.nextElement();
      Object value =  request.getHeader(name);
      sortem.add(padStringWidth(name,longest)+" = "+value);
    }
    print(sortem,out);
  }
%>

<b>Server System Properties</b>
<%
  {
    Collection sortem = new TreeSet();
    int longest = getLongest(System.getProperties().propertyNames());
    Enumeration properties = System.getProperties().propertyNames();
    while(properties.hasMoreElements()) {
      name = (String)properties.nextElement();
      Object value =  System.getProperty(name);
      sortem.add(padStringWidth(name,longest)+" = "+value);
    }
    print(sortem,out);
  }
%>

</pre>
<%!

  private static final int getLongest(Enumeration e)
  {
    int longest = -1;
    while(e.hasMoreElements()) {
      int length = e.nextElement().toString().length();
      if (length > longest) {
        longest = length;
      }
    }
    return longest;
  }


  private static final void print(Collection c, JspWriter out)
    throws JspException, IOException
  {
  	String t;
    Iterator i = c.iterator();
    while(i.hasNext()) {
    	t = i.next().toString();
      out.println(t);
      allinfo_sb.append(t + "\n");
    }
  }

    public static String padStringWidth(String s, int i)
    {
        StringBuffer stringbuffer;
        if(s != null)
        {
            stringbuffer = new StringBuffer(s);
            stringbuffer.setLength(i);
            int j = s.length();
            for(int l = j; l < i; l++)
                stringbuffer.setCharAt(l, ' ');

        } else
        {
            stringbuffer = new StringBuffer(i);
            for(int k = 0; k < i; k++)
                stringbuffer.setCharAt(k, ' ');

        }
        return stringbuffer.toString();
    }

     public static String getLocalHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e)
        {
            return "N/A";
        }
    }

%>



<p>
<%/*}*/%>
</font>
</body>
</html>

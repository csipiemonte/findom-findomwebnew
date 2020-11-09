<%@ page import="java.util.Enumeration,
                 java.io.ByteArrayOutputStream,
                 java.io.ObjectOutputStream,
                 java.io.NotSerializableException"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>Session Objects</title></head>
  <body>
    <h1>Session Objects</h1>
    <table border="1">
        <tr width="100%">
            <th>Key</th>
            <th>Value</th>
            <th>Data</th>
        </tr>
        <%
            long sessionSize=0L;
            Enumeration e=session.getAttributeNames();
            while(e.hasMoreElements()) {
                String attributeName=(String) e.nextElement();
        %>
        <tr>
            <td>
                <%= attributeName %>
            </td>
            <td>
                <%= session.getAttribute(attributeName) %>
            </td>
            <td>
                <%
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    ObjectOutputStream oos=new ObjectOutputStream(baos);
                    try {
                        oos.writeObject(session.getAttribute(attributeName));
                        sessionSize+=baos.size();
                        out.println("Size: "+baos.size()+" bytes");
                    } catch(NotSerializableException ex) {
                        out.println("<span style=\"color:red\">Unserializable</span>");
                    }
                %>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <p>Session size: <%= sessionSize %> bytes (estimated and minimum amount, excluding nonserializable elements)</p>
  </body>
</html>

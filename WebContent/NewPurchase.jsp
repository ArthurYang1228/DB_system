<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="models.*, java.sql.Connection, java.util.Enumeration, java.util.ArrayList, 
			java.util.Date, java.text.SimpleDateFormat, java.text.DateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增進貨</title>
<script type="text/javascript" language="javascript">
	function onSelected(beforeInventorys) {
		if(beforeInventorys == "0") {
			toUrl = "/TryIt/NewPurchase.jsp?inventorys=" + document.getElementById("inventoryID").value + "-" + 
				document.getElementById("quantity").value + "-" + document.getElementById("totalPrice").value 
				+ "-" + document.getElementById("supplier").value + ",";
		}
		else {
			toUrl = "/TryIt/NewPurchase.jsp?inventorys=" + beforeInventorys + document.getElementById("inventoryID").value + "-" + 
			document.getElementById("quantity").value + "-"
			+ document.getElementById("totalPrice").value + "-" + document.getElementById("supplier").value + ",";
		}
		window.location.replace(toUrl);
	}
</script>
<%@ include  file="InHead.html" %>
</head>
<body>
	<%@ include file="Header.html" %>
    <%
    request.setCharacterEncoding("UTF-8");
    Connection conn = Connector.getConn();
	PurchaseDAO purchaseDAO = new PurchaseDAO(conn);
	EmployeeDAO employeeDAO = new EmployeeDAO(conn);
	PurchaseDetailedDAO purchaseDetailedDAO = new PurchaseDetailedDAO(conn);
	InventoryDAO inventoryDAO = new InventoryDAO(conn);
	SupplierDAO supplierDAO = new SupplierDAO(conn);
	%>
	<%
		String inventorysInput = request.getParameter("inventorys");
	    String beforeInventorys;
		if(inventorysInput == null) {
			beforeInventorys = "0";
		} else {
			beforeInventorys = inventorysInput;
		}
	%>
	員工編號：<select name="eId" >
	<%
			for(Employee e : employeeDAO.getAllEmployees()) {
	%>
				<option value=<%=e.id %>><%=e.id+"  "+e.name %></option>
	<%
			}
	%>
			</select>
	
	<form action="javascript:onSelected('<%= beforeInventorys %>')">
	
			存貨：
			<select id="inventoryID">
	<%
			for(Inventory i : inventoryDAO.getAllInventorys()) {
	%>
				<option value=<%=i.id %>><%=i.name  %></option>
	<%
			}
	%>
			</select>
		
		        數量:<input type="number" id="quantity" min="1" required>
		        總價:<input type="number" id="totalPrice" min="1" required> 
		
		
			供應商：<select id="supplier"> 
	<%
			for(Supplier s : supplierDAO.getAllSuppliers()) {
				
	%>
				<option value=<%=s.id %>><%=s.name  %></option>
	<%
			}
	%>
			</select>
			<input type="submit" value="新增">
	</form>
	
	<hr>
	
	<% 
		
		if(inventorysInput != null) {
			String[] inventoryInfos = inventorysInput.split(",");
			for(String inventoryInfo: inventoryInfos) {
				String[] iInfo = inventoryInfo.split("-");
				int inventoryId = Integer.parseInt(iInfo[0]);
				int quantity = Integer.parseInt(iInfo[1]);
				int totalPrice = Integer.parseInt(iInfo[2]);
				int supplierId = Integer.parseInt(iInfo[3]);
				Inventory i = inventoryDAO.getInventory(inventoryId);
				Supplier s = supplierDAO.getSupplier(supplierId);
	%>
				<p><%= i.name + " " + quantity + " " + totalPrice + " " + s.name %></p>
	<%
			}
		}
	%>
	
	
	
	<form action="NewPurchase.jsp" method="POST">
		<input type="text" name="postInventorysInput" value=<%= inventorysInput %> 
		style="display: None"></input>
		
	<%
			if(inventorysInput !=null){
	%>		
		<input type="submit" value="確認訂貨"> <a href="NewPurchase.jsp"> 全部刪除 </a>
	<%
			}
	%>
	</form>
	
	<%	
		String postInventorysInput = request.getParameter("postInventorysInput");
		String eId = request.getParameter("eId");
	
		if(postInventorysInput != null && eId != null) {
			String[] postInventory = postInventorysInput.split(",");
			
			Date date = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			
			Purchase purchase = new Purchase(dateFormat.format(date),
					new EmployeeDAO(conn).getEmployee(Integer.parseInt(eId)));
			for(String s: postInventory) {
				
				String[] s2 = s.split("-");
				int inventoryId = Integer.parseInt(s2[0]);
				
				int quantity = Integer.parseInt(s2[1]);
				int totalPrice = Integer.parseInt(s2[2]);
				int supplierId = Integer.parseInt(s2[3]);
				Inventory inventory = inventoryDAO.getInventory(inventoryId);
				purchase.addPurchaseDetailed(
					new PurchaseDetailed(quantity, totalPrice,
					inventory, supplierDAO.getSupplier(supplierId)));
				
			
				inventory.quantity += quantity;
				inventoryDAO.update(inventory);
			}
			purchaseDAO.insert(purchase);
			out.print("新增成功");
		}
	%>
</body>
</html>
